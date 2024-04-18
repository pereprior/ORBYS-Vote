package com.orbys.quizz.data.controllers.handlers

import android.content.Context
import com.orbys.quizz.R
import com.orbys.quizz.data.constants.DOWNLOAD_ENDPOINT
import com.orbys.quizz.data.constants.TIME_OUT_MESSAGE
import com.orbys.quizz.data.constants.USER_RESPONDED_MESSAGE
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.domain.models.Question
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

private const val HTTP_FILES_FOLDER = "/assets/"
private const val HTTP_FILES_NAME = "_index"
private const val HTTP_FILES_EXTENSION = ".html"
private const val QUESTION_PLACEHOLDER = "[QUESTION]"
private const val ANSWER_PLACEHOLDER = "[ANSWER"
private const val SEND_BUTTON_PLACEHOLDER = "[SEND]"
private const val LOGIN_TITLE_PLACEHOLDER = "[LOGIN_TITLE]"
private const val MULTIPLE_CHOICE = "multiple"
private const val SINGLE_CHOICE = "single"
private const val CSV_FILE_NAME = "data"
private const val TIME_FORMAT = "HH:mm:ss"
private const val DATE_FORMAT = "dd/MM/yyyy"
private const val DATE_TIME_FORMAT = "yyyyMMddHHmmss"

class FileHandler @Inject constructor(
    private val httpRepository: HttpRepositoryImpl,
    private val fileRepository: FileRepository,
    private val context: Context
) {

    fun setupRoutes(route: Route) {
        route.handleDownloadRoute()
    }

    private fun Route.handleDownloadRoute() = get(DOWNLOAD_ENDPOINT) {
        call.respondFile(fileRepository.getFile())
    }

    fun getFileContent(userIP: String): String? {
        return when {
            httpRepository.timeOut().value -> TIME_OUT_MESSAGE
            httpRepository.userResponded(userIP) && !httpRepository.getQuestion().isMultipleAnswers -> USER_RESPONDED_MESSAGE
            else -> loadHtmlFile(httpRepository.getQuestion().answerType.name)
        }
    }

    fun loadHtmlFile(answerType: String): String? {
        val filePath = "${answerType.lowercase(Locale.ROOT)}$HTTP_FILES_NAME$HTTP_FILES_EXTENSION"
        return this::class.java.getResource("$HTTP_FILES_FOLDER$filePath")?.readText()?.let {
            replace(it, httpRepository.getQuestion())
        }
    }

    fun createDataFile(choice: String, userIP: String) {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val dateTimeFormatter = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val time = timeFormatter.format(Calendar.getInstance().time)
        val dateTime = dateTimeFormatter.format(Calendar.getInstance().time)

        fileRepository.createFile("$CSV_FILE_NAME$dateTime")
        fileRepository.writeFile(
            date = date,
            time = time,
            ip = userIP,
            username = httpRepository.getUsernameByIp(userIP),
            question = httpRepository.getQuestion().question,
            answer = choice
        )
    }

    private fun replace(content: String, question: Question): String = content
        .replace(SEND_BUTTON_PLACEHOLDER, context.getString(R.string.save_button_placeholder))
        .replace(LOGIN_TITLE_PLACEHOLDER, context.getString(R.string.login_title_placeholder))
        .replace(QUESTION_PLACEHOLDER, question.question)
        .replaceAnswersNames(question)
        .replaceOtherFunctions(question)

    private fun String.replaceAnswersNames(question: Question): String {
        var result = this
        question.answers.forEachIndexed { index, answer ->
            result = result.replace("$ANSWER_PLACEHOLDER$index]", answer.answer.toString())
        }
        return result
    }

    private fun String.replaceOtherFunctions(question: Question): String {
        val answersToString = question.answers.joinToString(",") { it.answer.toString() }
        val multipleChoices = if (question.isMultipleChoices) MULTIPLE_CHOICE else SINGLE_CHOICE
        val multipleAnswers = if (question.isMultipleAnswers) MULTIPLE_CHOICE else SINGLE_CHOICE

        return this
            .replace("ANSWERS_STRING_PLACEHOLDER", answersToString)
            .replace("MULTIPLE_CHOICES_PLACEHOLDER", multipleChoices)
            .replace("MULTIPLE_ANSWERS_PLACEHOLDER", multipleAnswers)
            .replace("MULTIPLE_VOTING_ALERT_PLACEHOLDER", context.getString(R.string.multiple_voting_alert_placeholder))
    }

}