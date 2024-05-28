package com.orbys.vote.core.managers

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity

/**
 * Clase para gestionar los permisos de superposición.
 *
 * @property activity La actividad desde la que se llama a esta clase.
 */
class OverlayPermissionManager (private val activity: AppCompatActivity) {

    /** Comprueba y solicita en caso de no tener los permisos para mostrar elementos por encima de otras aplicaciones. */
    fun checkAndRequestPermission() {

        if(!Settings.canDrawOverlays(activity)) {
            // Abre la pantalla de permisos de superposición del dispositivo.
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${activity.packageName}")
            )

            activity.startActivity(intent)
        }

    }

}