package io.groovin.permx.sampleapp

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.groovin.permx.permX
import io.groovin.permx.sampleapp.databinding.MainActivityBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val permX by permX()

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater).apply {
            permButton.setOnClickListener {
                checkPermission()
            }
            composeButton.setOnClickListener {
                startComposableActivity()
            }
            setContentView(root)
        }
    }

    private fun checkPermission() {
        lifecycleScope.launch {
            val permList = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            val permResult = permX.requestPermission(permList)
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

    private fun startComposableActivity() {
        startActivity(Intent(this, ComposableActivity::class.java))
    }
}
