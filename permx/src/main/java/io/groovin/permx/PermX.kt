package io.groovin.permx

import android.app.Activity
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

@Composable
fun rememberPermX(): PermX {
    val context = LocalContext.current
    if (context !is ComponentActivity) throw NotSupportActivityException()
    return remember { PermX(context) }
}

class PermX(private val activity: ComponentActivity) {
    suspend fun requestPermission(
        permissions: Array<String>,
        key: String = "launchActivity_${UUID.randomUUID()}_${SystemClock.elapsedRealtime()}"
    ) = activity.requestPermission(permissions, key)
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun ComponentActivity.requestPermission(
    permissions: Array<String>,
    key: String = "launchActivity_${UUID.randomUUID()}_${SystemClock.elapsedRealtime()}"
): PermissionResult = suspendCancellableCoroutine { cont ->
    var permLauncher: ActivityResultLauncher<Array<String>>? = null
    permLauncher = activityResultRegistry.register(
        key,
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        cont.resume(onPermissionResult(this, result))
        permLauncher?.unregister()
    }.also {
        it.launch(permissions)
    }

    cont.invokeOnCancellation {
        kotlin.runCatching {
            permLauncher.unregister()
        }
    }
}


private fun onPermissionResult(activity: Activity, result: Map<String, Boolean>): PermissionResult {
    return result.toPermissionResult(activity)
}

private fun Map<String, Boolean>.toPermissionResult(activity: Activity): PermissionResult {
    val grantList = this.filter { it.value }.map { it.key }
    val allDeniedList = this.filter { !it.value }.map { it.key }
    val map = allDeniedList.groupBy { permission ->
        if (activity.shouldShowRequestPermissionRationale(permission)) "DENIED" else "EXPLAINED"
    }
    val deniedList = map["DENIED"]?.toList() ?: listOf()
    val shouldShowRequestPermissionRationaleList = map["EXPLAINED"]?.toList() ?: listOf()
    return PermissionResult(grantList, deniedList, shouldShowRequestPermissionRationaleList)
}
