package com.orbys.quizz.data.controllers.handlers

import android.content.Context
import android.util.Log
import com.orbys.quizz.R
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.utils.ServerUtils.Companion.QUESTION_ENDPOINT
import com.orbys.quizz.data.utils.ServerUtils.Companion.USER_ENDPOINT
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
    private val fileHandler: FileHandler,
    private val appContext: Context
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
            text = fileContent ?: appContext.getString(R.string.file_not_found_message),
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleSubmitRoute() = post("/submit") {
        val userIP = call.request.origin.remoteHost
        val choice = call.receiveParameters()["choice"]

        // Si aún no ha terminado el tiempo, se registra la respuesta
        if(!repository.timeOut().value) {
            // Si la respuesta no existe, se añade
            // Si la respuesta contiene una coma, no se añade ya que se trata de una respuesta multiple
            if (!repository.answerExists(choice) && choice?.contains(",") == false) {
                repository.addAnswer(choice)
            }

            repository.setPostInAnswerCount(choice)
            updateUserStatus(choice ?: "", userIP)
        }

        call.respondRedirect(QUESTION_ENDPOINT)
    }

    private fun Route.handleSuccessRoute() = get(QUESTION_ENDPOINT) {
        call.response.headers.append("Cache-Control", "no-store")
        call.respondText(
            text = appContext.getString(R.string.success_message),
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleNewUserRoute() = get(USER_ENDPOINT) {
        val fileContent = fileHandler.loadHtmlFile("login")

        call.respondText(
            text = fileContent ?: appContext.getString(R.string.file_not_found_message),
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleLoginRoute() = post("/login") {
        val userIP = call.request.origin.remoteHost
        val username = call.receiveParameters()["user"] ?: ""

        // Si el usuario no existe, se registra
        if (repository.userNotExists(userIP)) {
            repository.addUser(User(userIP, username))
        }

        call.respondRedirect("$QUESTION_ENDPOINT/{id}")
    }

    private fun updateUserStatus(choice: String, userIP: String) {
        // Si el usuario no existe, se registra
        try {
            if (repository.userNotExists(userIP)) {
                repository.addUser(User(userIP, repository.getUsernameByIp(userIP), true))
            } else {
                // Si ya existe el usuario, se marca como que ha respondido
                repository.setUserResponded(userIP)
            }

            fileHandler.createDataFile(choice, userIP)

        } catch (e: Exception) {
            Log.d("RESPONSE1", "ERROR: ${e.message}")
        }

    }

}