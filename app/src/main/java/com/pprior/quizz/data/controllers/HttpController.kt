package com.pprior.quizz.data.controllers

import com.pprior.quizz.data.constants.ANSWER_PLACEHOLDER
import com.pprior.quizz.data.constants.ENDPOINT
import com.pprior.quizz.data.constants.ERROR_MESSAGE
import com.pprior.quizz.data.constants.FILES_EXTENSION
import com.pprior.quizz.data.constants.FILES_FOLDER
import com.pprior.quizz.data.constants.FILES_NAME
import com.pprior.quizz.data.constants.QUESTION_PLACEHOLDER
import com.pprior.quizz.data.constants.SUCCESS_MESSAGE
import io.ktor.http.ContentType
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import com.pprior.quizz.data.repositories.HttpRepositoryImpl
import com.pprior.quizz.domain.models.Question
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import java.util.Locale
import javax.inject.Inject

/**
 * Controlador de preguntas que define las rutas para obtener y enviar preguntas.
 *
 * @property repository Repositorio para interactuar con los datos de las preguntas.
 */
class HttpController @Inject constructor(
    private val repository: HttpRepositoryImpl
) {
    // Ip del usuario que accede al servidor
    private lateinit var userIP: String
    // Pregunta lanzada por el servidor
    private lateinit var question: Question

    // Configuración de las rutas del servidor
    fun setupRoutes(route: Route) {
        route.handleGetQuestionRoute()
        route.handleSubmitRoute()
        route.handleSuccessRoute()
    }

    // Ruta para responder la pregunta lanzada por el servidor
    private fun Route.handleGetQuestionRoute() = get("$ENDPOINT/{questionName}") {
        userIP = call.request.origin.remoteHost
        question = repository.findQuestion(call.parameters["questionName"] ?: "")
        val fileContent = loadHtmlFile(question.answerType.name)

        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    // Ruta que recibe la respuesta a la pregunta
    private fun Route.handleSubmitRoute() = post("/submit") {
        if (repository.userNotExists(userIP)) {
            val choice = call.receiveParameters()["choice"]
            repository.setPostInAnswerCount(question, choice)
            repository.addUserToRespondedList(userIP)
        }

        call.respondRedirect(ENDPOINT)
    }

    // Ruta que se muestra al usuario cuando ha respondido a la pregunta
    private fun Route.handleSuccessRoute() = get(ENDPOINT) {
        call.response.headers.append("Cache-Control", "no-store")

        call.respondText(
            text = SUCCESS_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    /**
     * @return El contenido del archivo HTML correspondiente como una cadena de texto.
     */
    private fun loadHtmlFile(answerType: String): String? {
        val filePath = "${answerType.lowercase(Locale.ROOT)}$FILES_NAME$FILES_EXTENSION"

        return this::class.java.getResource("$FILES_FOLDER$filePath")?.readText()?.let {
            replacePlaceholders(it, question)
        }
    }

    /**
     * @param fileContent El contenido del archivo HTML a reemplazar.
     * @param question La pregunta actual.
     * @return El contenido del archivo HTML con los marcadores de posición reemplazados.
     */
    private fun replacePlaceholders(fileContent: String?, question: Question): String? {
        var content = fileContent

        // Poner el titulo con la pregunta correspondiente
        content = content?.replace(QUESTION_PLACEHOLDER, question.question)

        // Poner las respuestas correspondientes en el formulario
        question.answers.forEachIndexed { index, answer ->
            content = content?.replace("$ANSWER_PLACEHOLDER${index}]", answer.answer.toString())
        }

        return content
    }

}