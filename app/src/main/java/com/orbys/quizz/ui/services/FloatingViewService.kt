package com.orbys.quizz.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orbys.quizz.R
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.ui.controllers.LaunchServiceManager
import com.orbys.quizz.ui.view.widgets.LaunchQuestionView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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

        startService(Intent(this, HttpService::class.java))

        launchQuestionView = LaunchQuestionView(this).apply {
            setOnTouchListener(this)

            // Añadir el codigo qr a la vista
            manager.printQrCode(
                qrCode = findViewById(R.id.qrCode),
                qrUrl = findViewById(R.id.qrUrl)
            )

            // Añadir la pregunta a la vista
            CoroutineScope(Dispatchers.Main).launch {
                manager.bindWidget(launchQuestionView.binding)
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