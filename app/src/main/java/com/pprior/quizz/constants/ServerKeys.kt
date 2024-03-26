package com.pprior.quizz.constants

import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.Collections

val host = getLocalIpAddress()
const val SERVER_PORT = 8888
const val ENDPOINT = "/question"

// Devuelve la dirección IP local del dispositivo
private fun getLocalIpAddress(): String? {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (i in interfaces) {
            val addresses = Collections.list(i.inetAddresses)
            for (address in addresses) {
                if (!address.isLoopbackAddress && address is Inet4Address) {
                    return address.hostAddress
                }
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    return null
}