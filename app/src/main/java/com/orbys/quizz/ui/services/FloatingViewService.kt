package com.orbys.quizz.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orbys.quizz.data.services.HttpService

class FloatingViewService: Service() {

    override fun onCreate() {
        super.onCreate()

        startService(Intent(this, HttpService::class.java))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val question = intent?.getStringExtra("question") ?: ""

        LaunchQuestionView(this)

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopService(Intent(this, HttpService::class.java))

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}