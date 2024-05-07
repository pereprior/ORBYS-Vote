package com.orbys.quizz.core.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.showToastWithCustomView
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

/**
 * Clase para gestionar todas las operaciones relacionadas con la red.
 */
class NetworkManager {

    companion object {
        const val SERVER_PORT = 8888
        const val QUESTION_ENDPOINT = "/question"
        const val DOWNLOAD_ENDPOINT = "/download"
        const val USER_ENDPOINT = "/user"
    }

    // Devuelve la URL del servidor con el endpoint especificado
    fun getServerUrl(endpoint: String) = "http://${getLocalIpAddress()}:$SERVER_PORT$endpoint"

    fun checkNetworkOnActivity(activity: AppCompatActivity) {
        if (!isNetworkAvailable(activity)) {
            activity.showToastWithCustomView(activity.getString(R.string.no_network_error), Toast.LENGTH_LONG)
            activity.finish()
            return
        }
    }

    // Devuelve la dirección IP local del dispositivo que ejecuta la app
    private fun getLocalIpAddress() = try {
        getLocalIpFromNetworkInterfaces()
    } catch (ex: Exception) { null }

    /**
     * Comprueba si hay conexión a Internet.
     *
     * @param activity La actividad actual.
     * @return true si hay conexión a Internet.
     */
    private fun isNetworkAvailable(activity: AppCompatActivity): Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Comprueba si hay una conexión a Internet activa
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        // Comprueba si la conexión a Internet activa tiene la capacidad de conectarse a Internet
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    // Obtiene la dirección IP local del dispositivo que ejecuta la app
    private fun getLocalIpFromNetworkInterfaces(): String? {
        // Obtengo una lista de todas las interfaces de red en el dispositivo
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())

        for (i in interfaces) {
            // Obtengo una lista de todas las direcciones IP asociadas a la interfaz
            val addresses = Collections.list(i.inetAddresses)


            for (address in addresses) {
                // Si la direccion IP no es loopback y es IPv4, la retornamos
                if (address.isNonLoopbackIPv4Address()) {
                    return address.hostAddress
                }
            }
        }

        return null
    }

    // Comprueba si la direccion es IPv4 no loopback
    private fun InetAddress.isNonLoopbackIPv4Address() = !isLoopbackAddress && this is Inet4Address
}