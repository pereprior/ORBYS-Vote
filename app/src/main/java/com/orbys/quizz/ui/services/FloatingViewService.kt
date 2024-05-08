package com.orbys.quizz.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orbys.quizz.R
import com.orbys.quizz.ui.components.LaunchQuestionView
import com.orbys.quizz.ui.viewmodels.LaunchServiceManager
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

        startService(Intent(this, manager.getHttpService()::class.java))

        launchQuestionView = LaunchQuestionView(this).apply {
            setOnTouchListener(this)

            // Añadir el codigo qr a la vista
            manager.printQrCode(
                qrCode = findViewById(R.id.qrCode),
                qrUrl = findViewById(R.id.qrTitle)
            )

            // Añadir la pregunta a la vista
            manager.bindWidget(this.binding)
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