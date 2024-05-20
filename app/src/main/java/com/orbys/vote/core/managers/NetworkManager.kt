package com.orbys.vote.core.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * Clase para gestionar todas las operaciones relacionadas con la red.
 */
class NetworkManager {

    // Devuelve la URL del servidor con el endpoint especificado
    fun getServerWifiUrl(endpoint: String, isHotspot: Boolean = false) =
        if (getIpAddress(isHotspot).isNullOrEmpty()) null
        else "http://${getIpAddress(isHotspot)}:$SERVER_PORT$endpoint"

    suspend fun checkNetworkOnActivity(activity: AppCompatActivity) {
        withContext(Dispatchers.IO) {
            if (!isNetworkAvailable(activity)) {
                withContext(Dispatchers.Main) {
                    // Muestra un mensaje de error si no hay conexión a Internet
                    activity.showToastWithCustomView(activity.getString(R.string.no_network_error), Toast.LENGTH_LONG)
                }

                return@withContext
            }
        }
    }

    /**
     * Comprueba si hay conexión a Internet.
     *
     * @param activity La actividad actual.
     * @return true si hay conexión a Internet.
     */
    fun isNetworkAvailable(activity: AppCompatActivity): Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // Comprueba si hay una conexión a Internet activa
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        // Comprueba si la conexión a Internet activa tiene la capacidad de conectarse a Internet
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    /**
     * Obtiene la dirección IP local del dispositivo que ejecuta la app.
     *
     * @param hotspot Indica si se quiere obtener la dirección IP de un hotspot o no.
     * @return La dirección IP local del dispositivo.
     */
    private fun getIpAddress(hotspot: Boolean = false): String? {
        val ipAddresses = getLocalIpFromNetworkInterfaces()

        // Filtramos las direcciones IP basándonos en si queremos una dirección de hotspot o no
        ipAddresses.mapNotNull {
            if (hotspot) {
                // Si queremos una dirección de hotspot, devolvemos la primera que sea de hotspot
                if (it.isHotspotIPv4Address())
                    return it
            } else {
                // Si no queremos una dirección de hotspot, devolvemos la primera que no sea de hotspot
                if (!it.isHotspotIPv4Address())
                    return it
            }
        }

        return null
    }

    // Obtiene la lista con las direcciones IPv4 del dispositivo
    private fun getLocalIpFromNetworkInterfaces(): List<String?> {
        val interfaces = NetworkInterface.getNetworkInterfaces().toList()
        val ipAddresses = mutableListOf<String?>()

        for (i in interfaces) {
            // Obtengo una lista de todas las direcciones IP asociadas a la interfaz
            val addresses = i.inetAddresses.toList()

            for (address in addresses) {
                // Si la direccion IP no es loopback y es IPv4, la retornamos
                if (address.isNonLoopbackIPv4Address()) {
                    ipAddresses.add(address.hostAddress)
                }
            }
        }

        return ipAddresses
    }

    // Comprueba si la direccion es IPv4 no loopback
    private fun InetAddress.isNonLoopbackIPv4Address() = !isLoopbackAddress && this is Inet4Address

    // Comprueba si la dirección IP es una dirección de hotspot
    private fun String?.isHotspotIPv4Address() = this!!.startsWith("192.168.43") || this.startsWith("172.16")

    companion object {
        const val SERVER_PORT = 8888
        const val QUESTION_ENDPOINT = "/question"
        const val DOWNLOAD_ENDPOINT = "/download"
        const val USER_ENDPOINT = "/user"
    }
}