package com.orbys.quizz.data.utils

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.Collections

/**
 * Clase utilitaria para la configuración del servidor.
 */
class ServerUtils {

    companion object {
        const val SERVER_PORT = 8888
        const val QUESTION_ENDPOINT = "/question"
        const val DOWNLOAD_ENDPOINT = "/download"
        const val USER_ENDPOINT = "/user"
    }

    fun getServerUrl(endpoint: String) = "http://${getLocalIpAddress()}:$SERVER_PORT$endpoint"

    // Devuelve la dirección IP local del dispositivo que ejecuta la app
    private fun getLocalIpAddress() = try { getLocalIpFromNetworkInterfaces() } catch (ex: Exception) { null }

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