package com.orbys.quizz.data.controllers.handlers

import android.content.Context
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.getAnswerType
import com.orbys.quizz.core.extensions.getAnswers
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.HttpRepositoryImpl
import com.orbys.quizz.data.utils.ServerUtils.Companion.DOWNLOAD_ENDPOINT
import com.orbys.quizz.domain.models.Question
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

/**
 * Clase para manejar las operaciones de archivos del servidor.
 *
 * @property httpRepository Repositorio para operaciones HTTP.
 * @property fileRepository Repositorio para operaciones de archivos.
 * @property appContext Contexto de la aplicacion.
 */
class FileHandler @Inject constructor(
    private val httpRepository: HttpRepositoryImpl,
    private val appContext: Context
) {
    private val fileRepository = FileRepository.getInstance(appContext)
    private companion object {
        const val HTTP_FILES_FOLDER = "/assets"
        const val HTTP_FILES_NAME = "_index"
        const val HTTP_FILES_EXTENSION = ".html"
        const val MULTIPLE_CHOICE = "multiple"
        const val SINGLE_CHOICE = "single"
        const val CSV_FILE_NAME = "data"
        const val TIME_FORMAT = "HH:mm:ss"
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

    fun setupRoutes(route: Route) { route.handleDownloadRoute() }

    // Ruta de descarga del archivo de datos.
    private fun Route.handleDownloadRoute() = get(DOWNLOAD_ENDPOINT) {
        val file = fileRepository.getFile()

        checkPossibleErrors(file, call)

        call.respondFile(file)
    }

    fun deleteFile() { fileRepository.deleteFile() }

    fun loadHtmlFile(
        htmlName: String = httpRepository.getQuestionInfo().getAnswerType()
    ): String? {
        val fileName = "${htmlName.lowercase(Locale.ROOT)}$HTTP_FILES_NAME$HTTP_FILES_EXTENSION"

        return this::class.java.getResource("$HTTP_FILES_FOLDER/$fileName")?.readText()?.let {
            // Reemplaza los marcadores de posición del archivo HTML por los valores correspondientes.
            replace(it, httpRepository.getQuestionInfo())
        }
    }

    fun createDataFile(choice: String, userIP: String) {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val time = timeFormatter.format(Calendar.getInstance().time)

        fileRepository.createFile(
            fileName = CSV_FILE_NAME,
            question = httpRepository.getQuestionInfo(),
            answers = httpRepository.getAnswersAsString()
        )

        fileRepository.writeLine(
            date = date,
            time = time,
            ip = userIP,
            username = httpRepository.getUsernameByIp(userIP),
            answer = choice
        )
    }

    private suspend fun checkPossibleErrors(file: File, call: ApplicationCall) {
        // Si aun no se ha terminado el tiempo no se podra descargar el archivo.
        if (!httpRepository.isTimeOut()) call.respondRedirect("/error/3")
        // Si nadie a contestado la pregunta no se podra descargar el archivo.
        if (!file.exists()) call.respondRedirect("/error/6")
    }

    private fun replace(content: String, question: Question): String = content
        .replace("[SEND]", appContext.getString(R.string.send_button_placeholder))
        .replace("[LOGIN_TITLE]", appContext.getString(R.string.login_title_placeholder))
        .replace("[QUESTION]", question.question)
        .replace("[MAX_ANSWER]", question.maxNumericAnswer.toString())
        .replace("[SUCCESS_MESSAGE]", appContext.getString(R.string.success_message))
        .replaceAnswersNames(question)
        .replaceOtherFunctions(question)

    private fun String.replaceAnswersNames(question: Question): String {
        var result = this
        // Reemplaza los marcadores de posición de las respuestas por los valores correspondientes.
        question.getAnswers().forEachIndexed { index, answer ->
            result = result.replace("[ANSWER$index]", answer.answer)
        }
        return result
    }

    private fun String.replaceOtherFunctions(question: Question): String {
        // Si el usuario envia varias respuestas a la vez
        val answersToString = question.getAnswers().joinToString(";") { it.answer }
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