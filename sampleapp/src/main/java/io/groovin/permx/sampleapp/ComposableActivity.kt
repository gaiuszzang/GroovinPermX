package io.groovin.permx.sampleapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.groovin.permx.rememberPermX
import io.groovin.permx.sampleapp.theme.GroovinPermXTheme
import kotlinx.coroutines.launch

class ComposableActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                GroovinPermXTheme {
                    ComposableScreen()
                }
        }
    }
}

@Composable
fun ComposableScreen() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val permX = rememberPermX()

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
            .background(MaterialTheme.colorScheme.background),
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
