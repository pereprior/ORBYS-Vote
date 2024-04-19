package com.orbys.quizz.ui.services

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.provider.Settings
import com.orbys.quizz.data.services.HttpService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FloatingViewService: Service() {

    override fun onCreate() {
        super.onCreate()

        getPermission()

        // Iniciamos el servicio Http de la pregunta
        startService(Intent(this, HttpService::class.java))

        // Iniciamos la vista de la pregunta
        LaunchQuestionView(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun getPermission() {

        if(!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

    }

    override fun onBind(intent: Intent?): IBinder? = null
}