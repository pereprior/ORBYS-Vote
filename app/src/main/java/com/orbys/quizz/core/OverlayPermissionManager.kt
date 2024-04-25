package com.orbys.quizz.core

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

/**
 * Clase para gestionar los permisos de superposición.
 *
 * @property activity La actividad desde la que se llama a esta clase.
 */
class OverlayPermissionManager (
    private val activity: AppCompatActivity
) {

    fun checkAndRequestPermission() {
        if(!Settings.canDrawOverlays(activity)) {
            // Crea un intent para abrir la configuración de permisos de superposición para esta aplicación.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${activity.packageName}")
            )
            activity.startActivity(intent)
        }
    }

}