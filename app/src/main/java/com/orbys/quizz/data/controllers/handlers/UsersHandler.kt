package com.orbys.quizz.data.controllers.handlers

import com.orbys.quizz.data.constants.ERROR_MESSAGE
import com.orbys.quizz.data.constants.QUESTION_ENDPOINT
import com.orbys.quizz.data.constants.USER_ENDPOINT
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.User
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import javax.inject.Inject

class UsersHandler@Inject constructor(
    private val repository: HttpRepositoryImpl,
    private val fileHandler: FileHandler
) {
/*
    private fun Route.handleNewUserRoute() = get(USER_ENDPOINT) {
        if (repository.getQuestion().isAnonymous) {
            call.respondRedirect(QUESTION_ENDPOINT)
        }

        val fileContent = fileHandler.loadHtmlFile("login")
        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleLoginRoute() = post("/login") {
        if (repository.userNotExists(userIP)) {
            val choice = call.receiveParameters()["user"]
            username = choice ?: ""
            repository.addUser(User(userIP, username))
        }

        call.respondRedirect("$QUESTION_ENDPOINT/{id}")
    }

    private fun updateUserStatus(choice: String) {
        if (repository.userNotExists(userIP)) {
            repository.addUser(User(userIP, username, true))
        } else {
            repository.setUserResponded(userIP)
        }

        fileHandler.createDataFile(choice, userIP)
    }
    */
}