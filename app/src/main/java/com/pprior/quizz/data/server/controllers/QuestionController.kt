package com.pprior.quizz.data.server.controllers

import android.util.Log
import com.pprior.quizz.data.server.repositories.QuestionRepositoryImp
import io.ktor.http.ContentType
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.koin.java.KoinJavaComponent.inject
import java.io.BufferedReader
import java.io.InputStreamReader

fun Route.questionController() {

    val repository: QuestionRepositoryImp by inject(QuestionRepositoryImp::class.java)

    // Ruta para la página de respuesta de una pregunta
    get("/question") {
        val fileContent = try {
            InputStreamReader(application::class.java.getResourceAsStream("/assets/index.html")).use { reader ->
                BufferedReader(reader).use { it.readText() }
            }
        } catch (e: Exception) {
            Log.e("Quizz", "Error al leer el archivo index.html", e)
            null
        }

        if (fileContent != null) {
            call.respondText(
                text = fileContent,
                contentType = ContentType.Text.Html
            )
        } else {
            call.respondText(
                text = "Error: no se pudo leer el archivo index.html",
                contentType = ContentType.Text.Html
            )
        }
    }

    post("/submit") {
        Log.d("Quizz", "Entrando al método post")
        val parameters = call.receiveParameters()
        val choice = parameters["choice"]
        Log.d("Quizz", "Parámetros recibidos: $parameters")

        try {
            repository.setPostInAnswerCount(choice)
            Log.d("Quizz", "Respuesta: $choice")
            //Log.d("Quizz", "Numero de si: ${questionComponent.getAnswer().}")
            //Log.d("Quizz", "Numero de no: ${questionComponent.getAnswer().noCount}")
        } catch (e: Exception) {
            Log.e("Quizz", "Error al obtener la respuesta", e)
        }

        call.respondRedirect("/success")
    }

    get("/success") {
        call.respondText(
            text = "Gracias por tu respuesta",
            contentType = ContentType.Text.Html
        )
    }

}