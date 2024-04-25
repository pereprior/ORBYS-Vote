package com.orbys.quizz.core

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.orbys.quizz.R
import com.orbys.quizz.ui.components.showToastWithCustomView

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
                    storagePermissionDenied()
                }
            }
        }
    }

    private fun storagePermissionDenied() {
        // Se ejecuta cuando se deniega el permiso de almacenamiento.
        activity.showToastWithCustomView(activity.getString(R.string.storage_permission_denied), Toast.LENGTH_LONG)
        activity.finish()
    }

}