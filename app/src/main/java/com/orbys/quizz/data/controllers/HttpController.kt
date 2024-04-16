package com.orbys.quizz.data.controllers

import android.util.Log
import com.orbys.quizz.data.constants.ANSWER_PLACEHOLDER
import com.orbys.quizz.data.constants.DATA_FILE_NAME
import com.orbys.quizz.data.constants.QUESTION_ENDPOINT
import com.orbys.quizz.data.constants.ERROR_MESSAGE
import com.orbys.quizz.data.constants.FILES_EXTENSION
import com.orbys.quizz.data.constants.FILES_FOLDER
import com.orbys.quizz.data.constants.FILES_NAME
import com.orbys.quizz.data.constants.QUESTION_PLACEHOLDER
import com.orbys.quizz.data.constants.SUCCESS_MESSAGE
import com.orbys.quizz.data.constants.TIME_OUT_MESSAGE
import com.orbys.quizz.data.constants.USER_ENDPOINT
import com.orbys.quizz.data.constants.USER_RESPONDED_MESSAGE
import com.orbys.quizz.data.repositories.FileRepository
import io.ktor.http.ContentType
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.models.User
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
    private val repository: HttpRepositoryImpl,
    private val fileRepository: FileRepository
) {
    // datos del usuario que accede al servidor
    private lateinit var userIP: String
    private var username = "Anonymous"

    // Pregunta lanzada por el servidor
    private lateinit var question: Question

    // Configuración de las rutas del servidor
    fun setupRoutes(route: Route) {
        route.handleGetQuestionRoute()
        route.handleSubmitRoute()
        route.handleSuccessRoute()

        route.handleNewUserRoute()
        route.handleLoginRoute()
    }

    // Ruta para responder la pregunta lanzada por el servidor
    private fun Route.handleGetQuestionRoute() = get("$QUESTION_ENDPOINT/{id}") {
        // Obtenemos la IP del usuario que accede al servidor y la pregunta que va a contestar
        userIP = call.request.origin.remoteHost
        question = repository.getQuestion()

        val fileContent =
            if (repository.timeState().value) {
                // Si el tiempo para responder la pregunta se ha agotado
                TIME_OUT_MESSAGE
            } else if(repository.userResponded(userIP) && !question.isMultipleAnswers) {
                // Si el usuario ya ha respondido a la pregunta
                USER_RESPONDED_MESSAGE
            } else {
                // Cargar el archivo html correspondiente a la pregunta
                loadHtmlFile(question.answerType.name)
            }

        // Si la pregunta no es anonima y el usuario aun no existe, redirigir a la ruta de login
        if (!question.isAnonymous) {

            if (repository.userNotExists(userIP)) {
                call.respondRedirect(USER_ENDPOINT)
            }

        }

        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    // Ruta que recibe la respuesta a la pregunta
    private fun Route.handleSubmitRoute() = post("/submit") {
        // Recibimos la respuesta del usuario
        val choice = call.receiveParameters()["choice"]

        // Actualizamos el contador de respuestas
        repository.setPostInAnswerCount(choice)

        fileRepository.createFile(DATA_FILE_NAME)
        fileRepository.writeFile(
            date = "",
            time = "",
            ip = userIP,
            username = username,
            question = question.question,
            answer = choice ?: ""
        )

        if (repository.userNotExists(userIP)) {
            // Si el usuario no existe, lo añadimos a la lista
            repository.addUser(User(userIP, username, true))
        } else {
            // Si ya existe, actualizamos su estado de respuesta
            repository.setUserResponded(userIP)
        }

        call.respondRedirect(QUESTION_ENDPOINT)
    }

    // Ruta que se muestra al usuario cuando ha respondido a la pregunta
    private fun Route.handleSuccessRoute() = get(QUESTION_ENDPOINT) {
        call.response.headers.append("Cache-Control", "no-store")
        username = "Anonymous"

        if (question.isMultipleAnswers) {
            // Si la pregunta se puede contestar varias veces, redirigir a la misma pregunta
            call.respondRedirect("$QUESTION_ENDPOINT/{id}")
        }

        call.respondText(
            text = SUCCESS_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    // Ruta para que el usuario se registre
    private fun Route.handleNewUserRoute() = get(USER_ENDPOINT) {
        if (question.isAnonymous) {
            // Si la pregunta es anonima, redirigir a la ruta de la pregunta sin login
            call.respondRedirect(QUESTION_ENDPOINT)
        }

        val fileContent = loadHtmlFile("login")

        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    // Ruta para que el usuario se loguee
    private fun Route.handleLoginRoute() = post("/login") {
        if (repository.userNotExists(userIP)) {
            val choice = call.receiveParameters()["user"]

            // Si el usuario no existe, lo añadimos a la lista
            username = choice ?: ""
            repository.addUser(User(userIP, username))
        }

        call.respondRedirect("$QUESTION_ENDPOINT/{id}")
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

        // Convertir la lista de respuestas a formato csv
        val answersToString = question.answers.joinToString(",") { it.answer.toString() }
        //  Reemplazar el marcador de posición en el script de la web
        content = content?.replace("ANSWERS_STRING_PLACEHOLDER", answersToString)

        val multipleChoices = if (question.isMultipleChoices) "multiple" else "single"
        Log.d("HttpController:", "Type choice: $multipleChoices")

        return content
    }

}