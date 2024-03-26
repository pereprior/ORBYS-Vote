package com.pprior.quizz.data.server.controllers

import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject
import com.pprior.quizz.data.server.repositories.QuestionRepositoryImp
import io.ktor.server.plugins.origin

/**
 * Controlador de preguntas que define las rutas para obtener y enviar preguntas.
 *
 * @receiver Route La ruta en la que se definen las rutas del controlador.
 */
fun Route.questionController() {

    // Repositorio para gestionar las respuestas de los usuarios.
    val repository: QuestionRepositoryImp by inject(QuestionRepositoryImp::class.java)

    get("/question") {
        val fileContent = this::class.java.getResource("/assets/index.html")?.readText()
        // Responde con el contenido del archivo index.html o un mensaje de error si el archivo no se puede leer.
        call.respondText(
            text = fileContent ?: "Error: no se pudo leer el archivo index.html",
            contentType = ContentType.Text.Html
        )
    }

    post("/submit") {
        val userIP = call.request.origin.remoteHost

        // Comprueba si el usuario ya ha respondido.
        if (repository.userNotExists(userIP)) {
            val choice = call.receiveParameters()["choice"]

            // Actualiza el recuento de respuestas en el repositorio.
            repository.setPostInAnswerCount(choice)

            // Añade la dirección IP del usuario a la lista de usuarios que ya han respondido.
            repository.addUserToRespondedList(userIP)
        }

        call.respondRedirect("/success")
    }

    get("/success") {
        // Responde con un mensaje de éxito al enviar la respuesta.
        call.respondText(
            text = "Gracias por tu respuesta",
            contentType = ContentType.Text.Html
        )
    }
}