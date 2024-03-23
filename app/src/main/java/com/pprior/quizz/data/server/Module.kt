package com.pprior.quizz.data.server

import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import io.ktor.http.ContentType.Text.Html

// Configura el servidor http
fun Application.module() {
    configureRouting()
    configureTemplating()
}

// Configura las rutas del servidor http
fun Application.configureRouting() {
    routing {
        // prueba
        get("/") {
            call.respondText("Hello, world!")
        }

        // Ruta para la página de respuesta de una pregunta
        get("/question") {
            val fileContent = this::class.java.classLoader
                ?.getResourceAsStream("static/templates/index.html")
                ?.bufferedReader()
                ?.use { it.readText() }

            call.respondText(
                text = fileContent ?: "",
                contentType = Html
            )
        }
    }
}

fun Application.configureTemplating() {
    // Establece el cargador de plantillas al directorio "static/templates".
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "static/templates")
    }
}