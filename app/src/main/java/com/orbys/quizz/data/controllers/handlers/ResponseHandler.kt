package com.orbys.quizz.data.controllers.handlers

import com.orbys.quizz.core.managers.NetworkManager.Companion.QUESTION_ENDPOINT
import com.orbys.quizz.core.managers.NetworkManager.Companion.USER_ENDPOINT
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.User
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import javax.inject.Inject

/**
 * Clase para gstionar las peticiones del servidor.
 *
 * @property repository Repositorio para operaciones HTTP.
 * @property fileHandler Gestor de archivos.
 */
class ResponseHandler@Inject constructor(
    private val repository: HttpRepositoryImpl,
    private val fileHandler: FileHandler
) {

    fun setupRoutes(route: Route) {

        route.apply {
            handleGetQuestionRoute()
            handleSubmitQuestionRoute()

            handleNewUserRoute()
            handleLoginRoute()
        }

    }

    /**
     * Ruta que muestra el formulario para contestar la pregunta.
     *
     * @return GET
     */
    private fun Route.handleGetQuestionRoute() = get(QUESTION_ENDPOINT) {
        val userIP = call.request.origin.remoteHost
        val fileContent = fileHandler.loadHtmlFile()

        // Si la pregunta no es anonima y el usuario no existe, redirigimos a la pagina de login
        if(!repository.getQuestionInfo().isAnonymous && repository.userNotExists(userIP))
            call.respondRedirect(USER_ENDPOINT)

        // Si el tiempo para responder se ha agotado, redirigimos a la pagina de error
        if (repository.isTimeOut())
            call.respondRedirect("/error/5")

        // Si la pregunta no es de multiples respuestas y el usuario ya ha respondido, redirigimos a la pagina de error
        if (repository.userResponded(userIP) && !repository.getQuestionInfo().isMultipleAnswers)
            call.respondRedirect("/error/7")

        try {
            call.respondText(
                text = fileContent!!,
                contentType = ContentType.Text.Html
            )
        } catch (e: Exception) { call.respondRedirect("/error/1") }
    }

    /**
     * Ruta que recibe y gestiona la respuesta del usuario.
     *
     * @return POST
     */
    private fun Route.handleSubmitQuestionRoute() = post("/submit") {
        val userIP = call.request.origin.remoteHost
        val choice = call.receiveParameters()["choice"]

        if (choice == "") return@post

        // Si aún no ha terminado el tiempo, se registra la respuesta
        if(!repository.isTimeOut()) {
            answerRegister(choice, userIP)
        }

        call.respond(HttpStatusCode.OK)
    }

    /**
     * Ruta que muestra el formulario para acceder a la pregunta con nombre de usuario.
     *
     * @return GET
     */
    private fun Route.handleNewUserRoute() = get(USER_ENDPOINT) {
        // Pintamos el popup de error por si el usuario que introduce ya existe
        val fileContent = fileHandler.loadHtmlFile("login")

        try {
            call.respondText(
                text = fileContent!!,
                contentType = ContentType.Text.Html
            )
        } catch (e: Exception) { call.respondRedirect("/error/1") }
    }

    /**
     * Ruta que recibe y gestiona el nombre de usuario.
     *
     * @return POST
     */
    private fun Route.handleLoginRoute() = post("/login") {
        val userIP = call.request.origin.remoteHost
        val username = call.receiveParameters()["user"] ?: ""

        // Si ya hay un usuario con el mismo nombre, no se registra
        if (repository.usernameExists(username)) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        // Si el usuario no existe, se registra
        if (repository.userNotExists(userIP))
            repository.addUserToList(User(userIP, username))

        call.respondRedirect(QUESTION_ENDPOINT)
    }

    private fun answerRegister(choice: String?, userIP: String) {
        // Si la respuesta no existe, se añade
        if (!repository.answerExists(choice) && choice?.contains(";") == false)
            repository.addAnswerToList(choice)

        repository.incAnswerCount(choice)

        // Si el usuario no existe, se registra
        if (repository.userNotExists(userIP))
            repository.addUserToList(User(userIP, repository.getUsernameByIp(userIP), true))
        else
            repository.setUserAsResponded(userIP)

        if (choice != null)
            fileHandler.createDataFile(choice, userIP)
    }

}