package com.orbys.quizz.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.R
import com.orbys.quizz.ui.components.showToastWithCustomView

/**
 * Clase para gestionar los permisos de red.
 *
 * @property activity La actividad desde la que se llama a esta clase.
 */
class NetworkPermissionManager (
    private val activity: AppCompatActivity
) {

    private val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun checkPermission() {
        if (!isNetworkAvailable()) {
            activity.showToastWithCustomView(activity.getString(R.string.no_network_error), Toast.LENGTH_LONG)
            activity.finish()
            return
        }
    }

    private fun isNetworkAvailable(): Boolean {
        // Comprueba si hay una conexión a Internet activa
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        // Comprueba si la conexión a Internet activa tiene la capacidad de conectarse a Internet
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

}