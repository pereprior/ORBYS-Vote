package com.pprior.quizz.data.server.controllers

import com.pprior.quizz.data.server.models.ResponseBase
import com.pprior.quizz.data.server.di.QuestionComponent
import org.koin.ktor.ext.inject
import io.ktor.server.routing.Route
import org.koin.java.KoinJavaComponent.inject

/*fun Route.questionController() {
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
}*/