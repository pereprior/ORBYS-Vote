package com.orbys.quizz.data.utils

import com.orbys.quizz.core.managers.NetworkManager
import javax.inject.Inject

/**
 * Clase utilitaria para la configuración del servidor.
 */
class ServerUtils @Inject constructor(
    private val networkManager: NetworkManager
) {

    companion object {
        const val SERVER_PORT = 8888
        const val QUESTION_ENDPOINT = "/question"
        const val DOWNLOAD_ENDPOINT = "/download"
        const val USER_ENDPOINT = "/user"
    }

    fun getServerUrl(endpoint: String) = "http://${networkManager.getLocalIpAddress()}:$SERVER_PORT$endpoint"
}