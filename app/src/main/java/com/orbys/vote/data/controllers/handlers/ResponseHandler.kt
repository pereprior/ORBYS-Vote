package com.orbys.vote.data.controllers.handlers

import android.content.Context
import com.orbys.vote.core.extensions.ERROR_ENDPOINT
import com.orbys.vote.core.extensions.QUESTION_ENDPOINT
import com.orbys.vote.core.extensions.USER_ENDPOINT
import com.orbys.vote.core.extensions.getAnswerType
import com.orbys.vote.data.repositories.ClientRepositoryImpl
import com.orbys.vote.data.repositories.QuestionRepositoryImpl
import com.orbys.vote.domain.models.Client
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
 * Clase para gestionar las peticiones de los clientes del servidor http.
 *
 * @param appContext Contexto de la aplicación.
 */
class ResponseHandler@Inject constructor(appContext: Context) {

    private val questionRepository = QuestionRepositoryImpl.getInstance()
    private val usersRepository = ClientRepositoryImpl.getInstance()
    private val fileHandler = FileHandler(appContext)

    fun setupRoutes(route: Route) {
        route.apply {
            handleGetQuestionRoute()
            handleSubmitQuestionRoute()

            handleNewUserRoute()
            handleLoginRoute()
        }
    }

    /** Ruta GET que proporciona al cliente el formulario para contestar la pregunta */
    private fun Route.handleGetQuestionRoute() = get(QUESTION_ENDPOINT) {
        val userIP = call.request.origin.remoteHost
        val question = questionRepository.getQuestion()
        val fileContent = fileHandler.loadHtmlFile(question.getAnswerType())

        // Si la pregunta no es anonima y el usuario no existe, redirigimos a la pagina de login
        if(!question.isAnonymous && usersRepository.clientNotExists(userIP))
            call.respondRedirect(USER_ENDPOINT)

        // Si el tiempo para responder se ha agotado, redirigimos a la pagina de error
        if (questionRepository.getTimerState())
            call.respondRedirect("$ERROR_ENDPOINT/5")

        // Si la pregunta no es de multiples respuestas y el usuario ya ha respondido, redirigimos a la pagina de error
        if (usersRepository.clientResponded(userIP) && !question.isMultipleAnswers)
            call.respondRedirect("$ERROR_ENDPOINT/7")

        try {
            call.respondText(fileContent!!, ContentType.Text.Html)
        } catch (e: Exception) {
            call.respondRedirect("$ERROR_ENDPOINT/1")
        }
    }

    /** Ruta POST que recibe y gestiona la respuesta del cliente */
    private fun Route.handleSubmitQuestionRoute() = post("/submit") {
        val userIP = call.request.origin.remoteHost
        val choice = call.receiveParameters()["choice"]

        // Si la respuesta está vacía, no se registra
        if (choice == "") return@post

        // Si aún no ha terminado el tiempo, se registra la respuesta
        if(!questionRepository.getTimerState())
            answerRegister(choice, userIP)

        call.respond(HttpStatusCode.OK)
    }

    /** Ruta GET que proporciona al cliente el formulario para introducir un nombre de usuario asociado a su dirección IP */
    private fun Route.handleNewUserRoute() = get(USER_ENDPOINT) {
        val fileContent = fileHandler.loadHtmlFile("login")

        try {
            call.respondText(fileContent!!, ContentType.Text.Html)
        } catch (e: Exception) {
            call.respondRedirect("$ERROR_ENDPOINT/1")
        }
    }

    /** Ruta que recibe y gestiona el nombre de usuario seleccionado por el cliente */
    private fun Route.handleLoginRoute() = post("/login") {
        val userIP = call.request.origin.remoteHost
        val username = call.receiveParameters()["user"] ?: ""

        // Si ya hay un usuario con el mismo nombre, no se registra
        if (usersRepository.usernameExists(username)) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        // Si el usuario no existe, se registra
        if (usersRepository.clientNotExists(userIP))
            usersRepository.addClient(Client(userIP, username))

        call.respondRedirect(QUESTION_ENDPOINT)
    }

    /**
     * Función para registrar una respuesta enviada al servidor por un cliente
     *
     * @param choice Respuesta seleccionada por el cliente.
     * @param user Identificador del cliente.
     */
    private fun answerRegister(choice: String?, user: String) {
        // Si la respuesta no existe, se añade
        if (!questionRepository.answerExists(choice) && choice?.contains(";") == false)
            questionRepository.addAnswer(choice)

        // Se incrementa el contador de la respuesta seleccionada
        questionRepository.incAnswerCount(choice)

        // Si el usuario no existe, se registra
        if (usersRepository.clientNotExists(user))
            usersRepository.addClient(Client(user, usersRepository.getUsernameByIp(user), true))
        else
            usersRepository.setClientResponded(user)

        // Si la respuesta no es nula, se añade al archivo de datos
        if (choice != null)
            fileHandler.createDataFile(choice, user)
    }

}