package com.orbys.quizz.ui.view.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.R
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.databinding.ActivityMainBinding
import com.orbys.quizz.ui.components.showToastWithCustomView
import com.orbys.quizz.ui.services.FloatingViewService
import com.orbys.quizz.ui.view.fragments.TypesQuestionFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * Actividad principal de la aplicación
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica si hay conexión a internet
        if (!isNetworkAvailable()) {
            this.showToastWithCustomView(getString(R.string.no_network_error), Toast.LENGTH_LONG)
            finish()
        }

        // Detiene los servicios si están en ejecución.
        stopService(Intent(this, HttpService::class.java))
        stopService(Intent(this, FloatingViewService::class.java))

        getPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        printFragments()
    }

    private fun printFragments() {

        with(binding) {

            closeButton.setOnClickListener {
                finish()
                exitProcess(0)
            }

            supportFragmentManager.beginTransaction().apply {
                // Fragmento con los tipos de preguntas
                replace(fragmentContainer.id, TypesQuestionFragment())
                commit()
            }
        }

    }

    private fun getPermission() {

        // Solicitar los permisos necesarios para permitir la superposición de ventanas
        if(!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}