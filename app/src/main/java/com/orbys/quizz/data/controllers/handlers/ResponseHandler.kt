package com.orbys.quizz.data.controllers.handlers

import com.orbys.quizz.data.constants.ERROR_MESSAGE
import com.orbys.quizz.data.constants.QUESTION_ENDPOINT
import com.orbys.quizz.data.constants.SUCCESS_MESSAGE
import com.orbys.quizz.data.constants.USER_ENDPOINT
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.User
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import javax.inject.Inject

/**
 * Clase para manejar las respuestas del servidor.
 *
 * @property repository Repositorio para operaciones HTTP.
 * @property fileHandler Manejador de archivos.
 */
class ResponseHandler@Inject constructor(
    private val repository: HttpRepositoryImpl,
    private val fileHandler: FileHandler
) {

    fun setupRoutes(route: Route) {

        route.apply {
            handleGetQuestionRoute()
            handleSubmitRoute()
            handleSuccessRoute()

            handleNewUserRoute()
            handleLoginRoute()
        }

    }

    private fun Route.handleGetQuestionRoute() = get("$QUESTION_ENDPOINT/{id}") {
        val userIP = call.request.origin.remoteHost

        val fileContent = fileHandler.getFileContent(userIP)

        // Si la pregunta no es anonima y el usuario no existe, redirigimos a la pagina de login
        if (!repository.getQuestion().isAnonymous && repository.userNotExists(userIP)) {
            call.respondRedirect(USER_ENDPOINT)
        }

        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleSubmitRoute() = post("/submit") {
        val userIP = call.request.origin.remoteHost
        val choice = call.receiveParameters()["choice"]

        // Si aún no ha terminado el tiempo, se registra la respuesta
        if(!repository.timeOut().value) {
            repository.setPostInAnswerCount(choice)
            updateUserStatus(choice ?: "", userIP)
        }

        call.respondRedirect(QUESTION_ENDPOINT)
    }

    private fun Route.handleSuccessRoute() = get(QUESTION_ENDPOINT) {
        call.response.headers.append("Cache-Control", "no-store")

        call.respondText(
            text = SUCCESS_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleNewUserRoute() = get(USER_ENDPOINT) {
        val fileContent = fileHandler.loadHtmlFile("login")

        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleLoginRoute() = post("/login") {
        val userIP = call.request.origin.remoteHost
        val username: String

        // Si el usuario no existe, se registra
        if (repository.userNotExists(userIP)) {
            val choice = call.receiveParameters()["user"]
            username = choice ?: ""
            repository.addUser(User(userIP, username))
        }

        call.respondRedirect("$QUESTION_ENDPOINT/{id}")
    }

    private fun updateUserStatus(choice: String, userIP: String) {
        // Si el usuario no existe, se registra
        if (repository.userNotExists(userIP)) {
            repository.addUser(User(userIP, repository.getUsernameByIp(userIP), true))
        } else {
            // Si ya existe el usuario, se marca como que ha respondido
            repository.setUserResponded(userIP)
        }

        fileHandler.createDataFile(choice, userIP)
    }

}