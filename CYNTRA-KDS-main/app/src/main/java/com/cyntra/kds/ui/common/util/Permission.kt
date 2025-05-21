package com.cyntra.kds.ui.common.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import com.cyntra.kds.R


fun ComponentActivity.multiplePermissions(
    onResult: (Map<String, Boolean>) -> Unit,
) = lazyOf(registerForActivityResult(RequestMultiplePermissions(), onResult))

fun Map<String, Boolean>.isAllGranted() = filterValues { it.not() }.isEmpty()

fun <I> ActivityResultLauncher<I>.request(input: I) = launch(input)


fun ComponentActivity.permission(
    permissionType: PermissionType,
    onRational: (PermissionType) -> Unit = {},
    onDenied: () -> Unit = {},
    onGranted: () -> Unit,
): Lazy<PermissionWrapper> {
    val registerForActivityResult = registerForActivityResult(RequestPermission()) {
        if (it) onGranted() else onDenied()
    }
    return lazyOf(PermissionWrapper(registerForActivityResult, permissionType,
        onRational))
}

fun ComponentActivity.permission(
    permissionType: PermissionType,
    onGranted: () -> Unit,
): Lazy<PermissionWrapper> {
    return permission(
        permissionType,
        onRational = {
            permissionDenied(it) {}
        },
        onDenied = {
            permissionDenied(permissionType) {}
        },
        onGranted = onGranted
    )
}


fun ComponentActivity.requestPermission(
    permission: PermissionWrapper,
) {
    if (shouldShowRequestPermissionRationale(permission.permissionType.permission))
        permission.onRational(permission.permissionType)
    else permission.request()

}


class PermissionWrapper(
    private val launcher: ActivityResultLauncher<String>,
    val permissionType: PermissionType,
    val onRational: (PermissionType) -> Unit,
) {
    fun request() = launcher.launch(permissionType.permission)
}


sealed class PermissionType(
    val permission: String,
    val permissionTitle: String,
    val permissionSubtitle: String,
) {
    object CAMERA : PermissionType(
        permission = Manifest.permission.CAMERA,
        permissionTitle = "Camera",
        permissionSubtitle = "To capture photos and videos, allow phone to access to your camera"
    )


    object MICROPHONE : PermissionType(Manifest.permission.RECORD_AUDIO,
        permissionTitle = "Microphone",
        permissionSubtitle = "To record audio, allow phone to access to your microphone")

    object LOCATION : PermissionType(Manifest.permission.ACCESS_FINE_LOCATION,
        permissionTitle = "Location",
        permissionSubtitle = "To capture location, allow phone to access to your location")


    object GPS : PermissionType(Manifest.permission.ACCESS_FINE_LOCATION,
        permissionTitle = "GPS",
        permissionSubtitle = "To capture location, allow phone to access to your GPS")

    object SMS : PermissionType(Manifest.permission.READ_SMS,
        permissionTitle = "Sms",
        permissionSubtitle = "To capture sms, allow phone to access to your sms")

    object CONTACT : PermissionType(Manifest.permission.READ_CONTACTS,
        permissionTitle = "Contact",
        permissionSubtitle = "To capture contact, allow phone to access to your contact")

    object STORAGE : PermissionType(Manifest.permission.READ_EXTERNAL_STORAGE,
        permissionTitle = "Storage",
        permissionSubtitle = "To capture storage, allow phone to access to your storage")
}


fun Activity.permissionDenied(permissionType: PermissionType, onAllow: (() -> Unit)?) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.mic_permission_title)
    builder.setMessage(R.string.mic_permission_required)
    builder.setPositiveButton(R.string.open_setting) { dialog, _ ->
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        this.startActivity(intent)
        dialog.cancel()
    }
    builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
        dialog.cancel()
    }
    builder.show()


}



fun Activity.multiplePermissionDenied() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.mic_permission_title)
    builder.setMessage(R.string.mic_permission_required)
    builder.setPositiveButton(R.string.open_setting) { dialog, _ ->
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        this.startActivity(intent)
        dialog.cancel()
    }
    builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
        dialog.cancel()
    }
    builder.show()

}