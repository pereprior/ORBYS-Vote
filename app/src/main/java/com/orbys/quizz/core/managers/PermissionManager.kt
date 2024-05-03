package com.orbys.quizz.core.managers

import androidx.appcompat.app.AppCompatActivity

/**
 * Clase para gestionar los permisos necesarios para la aplicación.
 *
 * @param activity La actividad desde la que se llama a esta clase.
 */
class PermissionManager(activity: AppCompatActivity) {

    private val storagePermissionManager = StoragePermissionManager(activity)
    private val overlayPermissionManager = OverlayPermissionManager(activity)

    // Comprueba y solicita los permisos necesarios.
    fun checkAndRequestPermissions() {
        storagePermissionManager.checkAndRequestPermission()
        overlayPermissionManager.checkAndRequestPermission()
    }

    // Maneja el resultado de la solicitud de permiso.
    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
    ) {
        storagePermissionManager.onRequestPermissionsResult(requestCode, grantResults, onPermissionGranted)
    }

}