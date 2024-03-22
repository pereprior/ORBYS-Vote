package com.pprior.quizz.data.server

import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.*
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import io.ktor.http.ContentType.Text.Html
import java.io.BufferedReader
import java.io.InputStreamReader

fun Application.module() {
    configureRouting()
    configureTemplating()
}

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, world!")
        }

        get("/question") {
            val inputStream = this::class.java.classLoader?.getResourceAsStream("static/templates/index.html")
            val fileContent = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }

            call.respondText(
                text = fileContent,
                contentType = Html
            )
        }
    }
}

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "static/templates")
    }
}