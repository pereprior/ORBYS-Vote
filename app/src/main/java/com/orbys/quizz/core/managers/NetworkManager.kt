package com.orbys.quizz.core.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.showToastWithCustomView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.reflect.InvocationTargetException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

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

    private var localIpAddress: String? = null

    // Devuelve la URL del servidor con el endpoint especificado
    fun getServerUrl(endpoint: String) = "http://${getLocalIpAddress()}:$SERVER_PORT$endpoint"

    /**
     * Comprueba si hay conexión a Internet y muestra un mensaje de error si no la hay.
     *
     * @param activity La actividad actual.
     */
    suspend fun checkNetworkOnActivity(activity: AppCompatActivity) {
        withContext(Dispatchers.IO) {
            if (!isNetworkAvailable(activity)) {
                withContext(Dispatchers.Main) {
                    activity.showToastWithCustomView(activity.getString(R.string.no_network_error), Toast.LENGTH_LONG)
                    activity.finish()
                }

                return@withContext
            }
        }
    }

    /**
     * Devuelve las credenciales para conectarse al dispositivo mediante hotspot.
     *
     * @param context El contexto actual.
     */
    fun getHotspotCredentials(context: Context): Pair<String?, String?> {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        try {
            val method = wifiManager.javaClass.getDeclaredMethod("getWifiApConfiguration")
            method.isAccessible = true
            val wifiConfig = method.invoke(wifiManager) as Any
            val ssidField = wifiConfig.javaClass.getDeclaredField("SSID")
            ssidField.isAccessible = true
            val ssid = ssidField.get(wifiConfig) as String
            val passwordField = wifiConfig.javaClass.getDeclaredField("preSharedKey")
            passwordField.isAccessible = true
            val password = passwordField.get(wifiConfig) as String
            return Pair(ssid.removeSurrounding("\""), password)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        return Pair(null, null)
    }

    // Devuelve la dirección IP local del dispositivo que ejecuta la app
    private fun getLocalIpAddress(): String? {
        if (localIpAddress == null) {
            localIpAddress = try {
                getLocalIpFromNetworkInterfaces()
            } catch (ex: Exception) { null }
        }

        return localIpAddress
    }

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
        val interfaces = NetworkInterface.getNetworkInterfaces().toList()

        for (i in interfaces) {
            // Obtengo una lista de todas las direcciones IP asociadas a la interfaz
            val addresses = i.inetAddresses.toList()

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