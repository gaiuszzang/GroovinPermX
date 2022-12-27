package io.groovin.permx.sampleapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showDialog(
    message: String,
    okayListener: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("Okay") { _, _ ->
            okayListener?.invoke()
        }
        setNegativeButton("Cancel", null)
    }.show()
}

fun getPermissionRequestRationaleMessage(deniedList: List<String>): String {
    val stringBuilder = StringBuilder()
    stringBuilder.append("Following Permissions are denied.\n\n")
    deniedList.forEach {
        stringBuilder.append("$it\n")
    }
    stringBuilder.append("\nPlease enable Permission from Settings.")
    return stringBuilder.toString()
}

fun Context.startApplicationDetailSettingsActivity() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${packageName}"))
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}