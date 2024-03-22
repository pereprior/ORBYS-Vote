package com.pprior.quizz.data.server.controllers

import com.pprior.quizz.data.server.models.ResponseBase
import com.pprior.quizz.data.server.di.QuestionComponent
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Route
import org.koin.ktor.ext.inject
import io.ktor.routing.get

fun Route.questionController() {
    val questionComponent by inject<QuestionComponent>()

    get("/questions/{title}") {
        val title = call.parameters["title"]

        if (title != null) {
            val question = questionComponent.getQuestion(title)
            call.respond(
                ResponseBase(
                    status = 200,
                    data = question
                )
            )
        } else {
            call.respond(
                ResponseBase<Unit>(
                    status = 400,
                    message = "Title parameter is missing"
                )
            )
        }
    }
}