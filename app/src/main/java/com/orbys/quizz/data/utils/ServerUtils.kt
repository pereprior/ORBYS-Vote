package com.orbys.quizz.data.utils

import com.orbys.quizz.core.network.NetworkManager

/**
 * Clase utilitaria para la configuración del servidor.
 */
class ServerUtils {

    private val networkManager = NetworkManager()

    companion object {
        const val SERVER_PORT = 8888
        const val QUESTION_ENDPOINT = "/question"
        const val DOWNLOAD_ENDPOINT = "/download"
        const val USER_ENDPOINT = "/user"
    }

    fun getServerUrl(endpoint: String) = "http://${networkManager.getLocalIpAddress()}:$SERVER_PORT$endpoint"
}