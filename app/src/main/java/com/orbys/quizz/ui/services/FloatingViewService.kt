package com.orbys.quizz.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FloatingViewService: Service() {

    @Inject
    lateinit var usersRepositoryImpl: UsersRepositoryImpl

    override fun onCreate() {
        super.onCreate()

        // Iniciamos el servicio Http de la pregunta
        startService(Intent(this, HttpService::class.java))

        // Iniciamos la vista de la pregunta
        LaunchQuestionView(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        stopService(Intent(this, HttpService::class.java))
        usersRepositoryImpl.clearRespondedUsers()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}