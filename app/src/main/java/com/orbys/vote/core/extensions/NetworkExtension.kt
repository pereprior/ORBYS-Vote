package com.orbys.vote.core.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import com.orbys.vote.databinding.FragmentQrCodeBinding
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

/** Comprueba si la direccion es IPv4 no loopback */
private fun InetAddress.isNonLoopbackIPv4Address() = !isLoopbackAddress && this is Inet4Address

/** Comprueba si la dirección IP pertenece a la red de Hotspot */
private fun String?.isHotspotIPv4Address() = this!!.startsWith("192.168.43") || this.startsWith("172.16")

/**
 * Comprueba si el dispositivo tiene conexión a Internet.
 *
 * @param activity La actividad actual.
 */
fun AppCompatActivity.isNetworkAvailable(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // Comprueba si hay una conexión a Internet activa
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    // Comprueba si la conexión a Internet activa tiene la capacidad de conectarse a Internet
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

/**
 * Devuelve la URL del servidor para contestar a una pregunta lanzada.
 *
 * @param endpoint El endpoint al que se quiere acceder.
 * @param isHotspot Indica si se quiere obtener la dirección IP de un hotspot o no.
 */
fun FragmentQrCodeBinding.getServerUrl(endpoint: String, isHotspot: Boolean = false): String? {
    val ip = getIpAddress(isHotspot)

    // Si no tenemos una IP válida, devolvemos null
    return if (ip.isNullOrEmpty()) null
    else "http://$ip:$SERVER_PORT$endpoint"
}

/**
 * Obtiene la dirección IP local del dispositivo que ejecuta la app.
 *
 * @param hotspot Indica si se quiere obtener la dirección IP de un hotspot o no.
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

/** Obtiene la lista con las direcciones IPv4 del dispositivo */
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

/** Constantes del servidor */
const val SERVER_PORT = 8888
const val QUESTION_ENDPOINT = "/question"
const val DOWNLOAD_ENDPOINT = "/download"
const val USER_ENDPOINT = "/user"