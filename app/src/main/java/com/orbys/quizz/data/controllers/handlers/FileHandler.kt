package com.orbys.quizz.data.controllers.handlers

import android.content.Context
import com.orbys.quizz.R
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.utils.ServerUtils.Companion.DOWNLOAD_ENDPOINT
import com.orbys.quizz.domain.models.Question
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

/**
 * Clase para manejar las operaciones de archivos del servidor.
 *
 * @property httpRepository Repositorio para operaciones HTTP.
 * @property fileRepository Repositorio para operaciones de archivos.
 * @property appContext Contexto de la aplicación.
 */
class FileHandler @Inject constructor(
    private val httpRepository: HttpRepositoryImpl,
    private val appContext: Context
) {
    private companion object {
        const val HTTP_FILES_FOLDER = "/assets/"
        const val HTTP_FILES_NAME = "_index"
        const val HTTP_FILES_EXTENSION = ".html"
        const val QUESTION_PLACEHOLDER = "[QUESTION]"
        const val ANSWER_PLACEHOLDER = "[ANSWER"
        const val MAX_ANSWER_PLACEHOLDER = "[MAX_ANSWER]"
        const val SEND_BUTTON_PLACEHOLDER = "[SEND]"
        const val LOGIN_TITLE_PLACEHOLDER = "[LOGIN_TITLE]"
        const val MULTIPLE_CHOICE = "multiple"
        const val SINGLE_CHOICE = "single"
        const val CSV_FILE_NAME = "data"
        const val TIME_FORMAT = "HH:mm:ss"
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

    private val fileRepository = FileRepository.getInstance(appContext)

    fun setupRoutes(route: Route) { route.handleDownloadRoute() }
    // Ruta de descarga del archivo de datos.
    private fun Route.handleDownloadRoute() = get(DOWNLOAD_ENDPOINT) {
        val file = fileRepository.getFile()

        // Si aun no se ha terminado el tiempo no se podra descargar el archivo.
        if (!httpRepository.timeOut().value) call.respondText(appContext.getString(R.string.time_in_message))
        // Si nadie a contestado la pregunta no se podra descargar el archivo.
        else if (!file.exists()) call.respondText(appContext.getString(R.string.no_response_message))
        else call.respondFile(file)
    }

    fun getFileContent(userIP: String) = when {
        // Si el tiempo de espera ha terminado muestra el mensaje de tiempo agotado.
        httpRepository.timeOut().value -> appContext.getString(R.string.time_out_message)
        // Si el usuario ya ha respondido y la pregunta no permite más respuestas muestra el mensaje de usuario respondido.
        httpRepository.userResponded(userIP) && !httpRepository.getQuestion().isMultipleAnswers -> appContext.getString(R.string.user_responded_message)
        // Carga el archivo HTML de la pregunta.
        else -> loadHtmlFile(httpRepository.getQuestion().answerType.name)
    }

    fun loadHtmlFile(answerType: String): String? {
        val fileName = "${answerType.lowercase(Locale.ROOT)}$HTTP_FILES_NAME$HTTP_FILES_EXTENSION"

        return this::class.java.getResource("$HTTP_FILES_FOLDER$fileName")?.readText()?.let {
            // Reemplaza los marcadores de posición del archivo HTML por los valores correspondientes.
            replace(it, httpRepository.getQuestion())
        }
    }

    fun createDataFile(choice: String, userIP: String) {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val time = timeFormatter.format(Calendar.getInstance().time)
        // Fecha y hora actual como indentificador unico del archivo.

        fileRepository.createFile(
            fileName = CSV_FILE_NAME,
            question = httpRepository.getQuestion(),
            answers = httpRepository.getQuestion().answers.value.map { it.answer.toString() }
        )

        fileRepository.writeLine(
            date = date,
            time = time,
            ip = userIP,
            username = httpRepository.getUsernameByIp(userIP),
            answer = choice
        )
    }

    fun deleteFile() { fileRepository.deleteFile() }

    private fun replace(content: String, question: Question): String = content
        .replace(SEND_BUTTON_PLACEHOLDER, appContext.getString(R.string.save_button_placeholder))
        .replace(LOGIN_TITLE_PLACEHOLDER, appContext.getString(R.string.login_title_placeholder))
        .replace(QUESTION_PLACEHOLDER, question.question)
        .replace(MAX_ANSWER_PLACEHOLDER, question.maxNumericAnswer.toString())
        .replaceAnswersNames(question)
        .replaceOtherFunctions(question)

    private fun String.replaceAnswersNames(question: Question): String {
        var result = this
        // Reemplaza los marcadores de posición de las respuestas por los valores correspondientes.
        question.answers.value.forEachIndexed { index, answer ->
            result = result.replace("$ANSWER_PLACEHOLDER$index]", answer.answer.toString())
        }
        return result
    }

    private fun String.replaceOtherFunctions(question: Question): String {
        // Si el usuario envia varias respuestas a la vez
        val answersToString = question.answers.value.joinToString(";") { it.answer.toString() }
        // Si la pregunta es de eleccion multiple o de respuesta multiple
        val multipleChoices = if (question.isMultipleChoices) MULTIPLE_CHOICE else SINGLE_CHOICE
        val multipleAnswers = if (question.isMultipleAnswers) MULTIPLE_CHOICE else SINGLE_CHOICE

        return this
            .replace("ANSWERS_STRING_PLACEHOLDER", answersToString)
            .replace("MULTIPLE_CHOICES_PLACEHOLDER", multipleChoices)
            .replace("MULTIPLE_ANSWERS_PLACEHOLDER", multipleAnswers)
            .replace("MULTIPLE_VOTING_ALERT_PLACEHOLDER", appContext.getString(R.string.multiple_voting_alert_placeholder))
    }

}