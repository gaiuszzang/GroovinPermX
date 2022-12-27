package io.groovin.permx.sampleapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.groovin.permx.LocalPermX
import io.groovin.permx.permX
import io.groovin.permx.sampleapp.theme.GroovinPermXTheme
import kotlinx.coroutines.launch

class ComposableActivity : ComponentActivity() {
    private val permX by permX()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalPermX provides permX) {
                GroovinPermXTheme {
                    ComposableScreen()
                }
            }
        }
    }
}

@Composable
fun ComposableScreen() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val permX = LocalPermX.current

    suspend fun checkPermission() {
        val permList = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permResult = permX.requestPermission(permList)
        with(context) {
            if (permResult.isAllGranted()) {
                showToast("Permission All Granted!")
            } else if (permResult.shouldShowRequestPermissionRationale()) {
                showDialog(getPermissionRequestRationaleMessage(permResult.getAllDeniedPermissionList())) {
                    startApplicationDetailSettingsActivity()
                }
            } else {
                showToast("Permission not Granted.")
            }
        }
    }

    //Composable Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                coroutineScope.launch {
                    checkPermission()
                }
            }
        ) {
            Text("ask Permission")
        }
    }
}
