package io.groovin.permx

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PermX(activity: Activity) {
    private var permContinuation: Continuation<PermissionResult>? = null
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null

    init {
        when(activity) {
            is AppCompatActivity -> init(activity)
            is ComponentActivity -> init(activity)
            else -> throw NotSupportActivityException()
        }
    }

    private fun init(activity: AppCompatActivity) {
        permissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onPermissionResult(activity, it)
        }
    }

    private fun init(activity: ComponentActivity) {
        permissionLauncher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onPermissionResult(activity, it)
        }
    }

    private fun onPermissionResult(activity: Activity, result: Map<String, Boolean>) {
        val permResult = result.toPermissionResult(activity)
        permContinuation?.resume(permResult)
        permContinuation = null
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

    suspend fun requestPermission(permissions: Array<String>): PermissionResult = suspendCoroutine {
        permContinuation = it
        permissionLauncher?.launch(permissions) ?: run {
            permContinuation = null
            throw NotInitializedException()
        }
    }
}