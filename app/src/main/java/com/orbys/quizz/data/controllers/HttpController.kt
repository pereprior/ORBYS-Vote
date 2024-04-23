package com.orbys.quizz.data.controllers

import com.orbys.quizz.data.controllers.handlers.FileHandler
import com.orbys.quizz.data.controllers.handlers.ResponseHandler
import io.ktor.server.routing.Route
import javax.inject.Inject

class HttpController @Inject constructor(
    private val responseHandler: ResponseHandler,
    private val fileHandler: FileHandler
) {

    fun setupRoutes(route: Route) {

        route.apply {
            // Respuestas del servidor http
            responseHandler.setupRoutes(this)

            // Descargar los datos del servidor
            fileHandler.setupRoutes(this)
        }

    }

    fun clearDataFile() { fileHandler.deleteFile() }
}