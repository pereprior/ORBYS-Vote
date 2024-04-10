package com.orbys.quizz.ui.services

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import com.orbys.quizz.databinding.ServiceLaunchQuestionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchService: Service() {

    private lateinit var binding: ServiceLaunchQuestionBinding
    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams

    override fun onCreate() {
        super.onCreate()

        // Inflar la vista
        binding = ServiceLaunchQuestionBinding.inflate(LayoutInflater.from(this))

        // Crear el WindowManager
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Configurar los LayoutParams
        layoutParams = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        // Posicionar la vista en la parte superior izquierda de la pantalla
        layoutParams.gravity = Gravity.TOP or Gravity.START
        layoutParams.x = 0
        layoutParams.y = 100

        // Agregar la vista al WindowManager
        windowManager.addView(binding.root, layoutParams)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remover la vista del WindowManager
        windowManager.removeView(binding.root)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}