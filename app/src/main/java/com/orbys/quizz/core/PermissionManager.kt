package com.orbys.quizz.core

import androidx.appcompat.app.AppCompatActivity

/**
 * Clase para gestionar los permisos necesarios para la aplicación.
 *
 * @property activity La actividad desde la que se llama a esta clase.
 */
class PermissionManager(activity: AppCompatActivity) {

    private val networkPermissionManager = NetworkPermissionManager(activity)
    private val storagePermissionManager = StoragePermissionManager(activity)
    private val overlayPermissionManager = OverlayPermissionManager(activity)

    fun checkAndRequestPermissions() {
        networkPermissionManager.checkPermission()
        storagePermissionManager.checkAndRequestPermission()
        overlayPermissionManager.checkAndRequestPermission()
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
    ) {
        storagePermissionManager.onRequestPermissionsResult(requestCode, grantResults, onPermissionGranted)
    }

}