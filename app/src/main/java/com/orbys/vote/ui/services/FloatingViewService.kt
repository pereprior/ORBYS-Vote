package com.orbys.vote.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orbys.vote.ui.components.LaunchQuestionView
import com.orbys.vote.ui.viewmodels.LaunchServiceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Servicio que muestra una vista flotante en la pantalla del dispositivo
 */
@AndroidEntryPoint
class FloatingViewService: Service() {

    @Inject
    lateinit var manager: LaunchServiceManager
    private lateinit var launchQuestionView: LaunchQuestionView

    override fun onCreate() {
        super.onCreate()

        val serviceIntent = Intent(this, manager.getHttpService()::class.java)
        startService(serviceIntent)

        launchQuestionView = LaunchQuestionView(this).apply {
            setOnTouchListener(this)

            // Añadir la pregunta a la vista
            with(binding) {
                manager.setQuestionElements(binding = this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Eliminar la vista del servicio
        launchQuestionView.windowManager.removeView(launchQuestionView)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_NOT_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
}