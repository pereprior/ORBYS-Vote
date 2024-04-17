package com.orbys.quizz.data.controllers

import com.orbys.quizz.data.constants.*
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.models.User
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.plugins.origin
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class HttpController @Inject constructor(
    private val repository: HttpRepositoryImpl,
    val fileRepository: FileRepository
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
            handleDownloadRoute()
        }
    }

    private fun Route.handleGetQuestionRoute() = get("$QUESTION_ENDPOINT/{id}") {
        userIP = call.request.origin.remoteHost

        val fileContent = getFileContent()

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

        val fileContent = loadHtmlFile("login")
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

    private fun Route.handleDownloadRoute() = get(DOWNLOAD_ENDPOINT) {
        call.respondFile(fileRepository.getFile())
    }

    private fun getFileContent(): String? {
        return when {
            repository.timeOut().value -> TIME_OUT_MESSAGE
            repository.userResponded(userIP) && !repository.getQuestion().isMultipleAnswers -> USER_RESPONDED_MESSAGE
            else -> loadHtmlFile(repository.getQuestion().answerType.name)
        }
    }

    private fun updateUserStatus(choice: String) {
        if (repository.userNotExists(userIP)) {
            repository.addUser(User(userIP, username, true))
        } else {
            repository.setUserResponded(userIP)
        }

        createDataFile(choice)
    }

    private fun loadHtmlFile(answerType: String): String? {
        val filePath = "${answerType.lowercase(Locale.ROOT)}$FILES_NAME$FILES_EXTENSION"
        return this::class.java.getResource("$FILES_FOLDER$filePath")?.readText()?.let {
            replacePlaceholders(it, repository.getQuestion())
        }
    }

    private fun replacePlaceholders(fileContent: String?, question: Question): String? {
        var content = fileContent

        content = content?.replace(QUESTION_PLACEHOLDER, question.question)
        question.answers.forEachIndexed { index, answer ->
            content = content?.replace("$ANSWER_PLACEHOLDER${index}]", answer.answer.toString())
        }

        val answersToString = question.answers.joinToString(",") { it.answer.toString() }
        content = content?.replace("ANSWERS_STRING_PLACEHOLDER", answersToString)

        val multipleChoices = if (question.isMultipleChoices) "multiple" else "single"
        content = content?.replace("MULTIPLE_CHOICES_PLACEHOLDER", multipleChoices)

        return content
    }

    private fun createDataFile(choice: String) {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val dateTimeFormatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val time = timeFormatter.format(Calendar.getInstance().time)
        val dateTime = dateTimeFormatter.format(Calendar.getInstance().time)

        fileRepository.createFile("$DATA_FILE_NAME$dateTime")
        fileRepository.writeFile(
            date = date,
            time = time,
            ip = userIP,
            username = repository.getUsernameByIp(userIP),
            question = repository.getQuestion().question,
            answer = choice
        )
    }

}