package com.orbys.quizz.core.managers

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Clase StoragePermissionManager para gestionar los permisos de almacenamiento.
 *
 * @property activity La actividad desde la que se llama a esta clase.
 */
class StoragePermissionManager(
    private val activity: AppCompatActivity
) {
    companion object {
        private const val STORAGE_PERMISSION_CODE = 2
    }

    fun checkAndRequestPermission() {
        // Si no tiene ya el permiso lo solicita
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
    ) {
        when (requestCode) {
            // Maneja el resultado de la solicitud de permiso.
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted()
                } else {
                    Log.d("StoragePermissionManager", "Storage Permission denied")
                    onPermissionGranted()
                }
            }
        }
    }

}