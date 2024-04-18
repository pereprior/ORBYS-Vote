package com.orbys.quizz.data.controllers

import com.orbys.quizz.data.constants.*
import com.orbys.quizz.data.controllers.handlers.FileHandler
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.User
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import javax.inject.Inject

class HttpController @Inject constructor(
    private val repository: HttpRepositoryImpl,
    private val fileHandler: FileHandler
) {
    private lateinit var userIP: String
    private var username = "Anonymous"

    fun setupRoutes(route: Route) {

        route.apply {
            handleGetQuestionRoute()
            handleSubmitRoute()
            handleSuccessRoute()

            handleNewUserRoute()
            handleLoginRoute()

            fileHandler.setupRoutes(this)
        }

    }

    private fun Route.handleGetQuestionRoute() = get("$QUESTION_ENDPOINT/{id}") {
        userIP = call.request.origin.remoteHost

        val fileContent = fileHandler.getFileContent(userIP)

        if (!repository.getQuestion().isAnonymous && repository.userNotExists(userIP)) {
            call.respondRedirect(USER_ENDPOINT)
        }

        call.respondText(
            text = fileContent ?: ERROR_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

    private fun Route.handleSubmitRoute() = post("/submit") {
        val choice = call.receiveParameters()["choice"]

        if(!repository.timeOut().value) {
            repository.setPostInAnswerCount(choice)
            updateUserStatus(choice ?: "")
        }

        call.respondRedirect(QUESTION_ENDPOINT)
    }

    private fun Route.handleSuccessRoute() = get(QUESTION_ENDPOINT) {
        call.response.headers.append("Cache-Control", "no-store")
        username = "Anonymous"

        if (repository.getQuestion().isMultipleAnswers) {
            call.respondRedirect("$QUESTION_ENDPOINT/{id}")
        }

        call.respondText(
            text = SUCCESS_MESSAGE,
            contentType = ContentType.Text.Html
        )
    }

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

}