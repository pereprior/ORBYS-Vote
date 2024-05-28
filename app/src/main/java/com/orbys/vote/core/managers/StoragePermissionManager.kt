package com.orbys.vote.core.managers

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Clase para gestionar los permisos de almacenamiento.
 *
 * @property activity La actividad desde la que se llama a esta clase.
 */
class StoragePermissionManager(private val activity: AppCompatActivity) {

    /** Comprueba y solicita en caso de no tener los permisos para escribir y guardar ficheros dentro del almacenamiento interno del dispositivo. */
    fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    /** Maneja el resultado de la solicitud de permiso. */
    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray,
        onPermissionGranted: () -> Unit,
    ) {
        when (requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted()
                } else {
                    // Si no se conceden los permisos, se muestra un mensaje de error pero se permite continuar.
                    Log.e("StoragePermissionManager", "Storage Permission denied")
                    onPermissionGranted()
                }
            }
        }
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 2
    }

}