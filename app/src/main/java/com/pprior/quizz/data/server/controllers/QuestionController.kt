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
    lateinit var userIP: String

    get("/question/{questionName}") {
        userIP = call.request.origin.remoteHost
        val questionName = call.parameters["questionName"]
        val question = repository.findQuestion(questionName ?: "")
        var fileContent: String? = null

        // Obtenemos el contenido del archivo html correspondiente al tipo de respuesta de la pregunta.
        when (question.answerType?.name) {
            "YESNO" -> {
                fileContent = this::class.java.getResource("/assets/yesno_index.html")?.readText()
                fileContent = fileContent?.replace("[YES]", question.answers[0].answer.toString())
                fileContent = fileContent?.replace("[NO]", question.answers[1].answer.toString())
            }
            "STARS" -> fileContent = this::class.java.getResource("/assets/stars_index.html")?.readText()
            "BAR" -> fileContent = this::class.java.getResource("/assets/bar_index.html")?.readText()
            "OTHER" -> {
                fileContent = this::class.java.getResource("/assets/other_index.html")?.readText()
                fileContent = fileContent?.replace("[FIRST]", question.answers[0].answer.toString())
                fileContent = fileContent?.replace("[SECOND]", question.answers[1].answer.toString())
                fileContent = fileContent?.replace("[THIRD]", question.answers[2].answer.toString())
            }
        }

        // Reemplaza el marcador de posición con la pregunta real
        fileContent = fileContent?.replace("[QUESTION]", question.question)

        // Responde con el contenido del archivo yesno_index.html o un mensaje de error si el archivo no se puede leer.
        call.respondText(
            text = fileContent ?: "Error: no se pudo leer el archivo html",
            contentType = ContentType.Text.Html
        )

    }

    post("/submit") {
        // Comprueba si el usuario ya ha respondido.
        /*if (repository.userNotExists(userIP)) {
            val choice = call.receiveParameters()["choice"]

            // Actualiza el recuento de respuestas en el repositorio.
            repository.setPostInAnswerCount(choice)

            // Añade la dirección IP del usuario a la lista de usuarios que ya han respondido.
            repository.addUserToRespondedList(userIP)
        }*/
        val choice = call.receiveParameters()["choice"]
        repository.setPostInAnswerCount(choice)
        repository.addUserToRespondedList(userIP)

        call.respondRedirect("/question/success")
    }

    get("/question/success") {

        call.response.headers.append("Cache-Control", "no-store")
        // Responde con un mensaje de éxito al enviar la respuesta.
        call.respondText(
            text = "Gracias por tu respuesta",
            contentType = ContentType.Text.Html
        )
    }

}