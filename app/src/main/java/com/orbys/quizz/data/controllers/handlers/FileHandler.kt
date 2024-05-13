package com.orbys.quizz.data.controllers.handlers

import android.content.Context
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.getAnswerType
import com.orbys.quizz.core.extensions.getAnswers
import com.orbys.quizz.core.managers.NetworkManager.Companion.DOWNLOAD_ENDPOINT
import com.orbys.quizz.data.repositories.FileRepository
import com.orbys.quizz.data.repositories.QuestionRepositoryImpl
import com.orbys.quizz.data.repositories.UsersRepositoryImpl
import com.orbys.quizz.domain.models.Question
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

/**
 * Clase para gestionar las operaciones con los archivos del servidor.
 *
 * @property questionRepository Repositorio de preguntas.
 * @property usersRepository Repositorio de usuarios.
 * @property appContext Contexto de la aplicacion.
 */
class FileHandler @Inject constructor(
    private val questionRepository: QuestionRepositoryImpl,
    private val usersRepository: UsersRepositoryImpl,
    private val appContext: Context
) {
    private val fileRepository = FileRepository.getInstance(appContext)
    private companion object {
        const val HTTP_FILES_FOLDER = "/assets"
        const val HTTP_FILES_PLACEHOLDER = "_index"
        const val HTTP_FILES_EXTENSION = ".html"
        const val MULTIPLE_CHOICE = "multiple"
        const val SINGLE_CHOICE = "single"
        const val CSV_FILE_NAME = "data"
        const val TIME_FORMAT = "HH:mm:ss"
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

    fun setupRoutes(route: Route) {

        route.apply {
            handleDownloadRoute()
            staticContent()
        }

    }

    /**
     * Ruta para obtener el archivo de estilos CSS.
     *
     * @return GET
     */
    private fun Route.staticContent() {
        get("/styles.css") {
            val css = this::class.java.classLoader?.getResource("assets/styles.css")?.readText()
            call.respondText(css ?: "", ContentType.Text.CSS)
        }
    }

    /**
     * Ruta para descargar el archivo con los datos de la pregunta.
     *
     * @return GET
     */
    private fun Route.handleDownloadRoute() = get(DOWNLOAD_ENDPOINT) {
        checkPossibleErrors(fileRepository.getFile(), call)
        call.respondFile(fileRepository.getFile())
    }

    fun deleteFile() { fileRepository.deleteFile() }

    /**
     * Carga el archivo HTML correspondiente a la pregunta.
     *
     * @param htmlName Nombre del archivo HTML.
     * @return Contenido del archivo HTML.
     */
    fun loadHtmlFile(
        htmlName: String = questionRepository.getQuestion().getAnswerType()
    ): String? {
        val name = if (htmlName == "yesno") "boolean" else htmlName.lowercase(Locale.ROOT)
        val fileName = name + HTTP_FILES_PLACEHOLDER + HTTP_FILES_EXTENSION

        return this::class.java.getResource("$HTTP_FILES_FOLDER/$fileName")?.readText()?.let {
            // Reemplaza los marcadores de posición del archivo HTML por los valores correspondientes.
            replace(it, questionRepository.getQuestion())
        }
    }

    /**
     * Crea el archivo de datos y añade la respuesta del usuario.
     *
     * @param choice Respuesta del usuario.
     * @param userIP IP del usuario que ha contestado.
     */
    fun createDataFile(choice: String, userIP: String) {
        val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val timeFormatter = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val date = dateFormatter.format(Calendar.getInstance().time)
        val time = timeFormatter.format(Calendar.getInstance().time)

        fileRepository.createFile(
            fileName = CSV_FILE_NAME,
            question = questionRepository.getQuestion(),
            answers = questionRepository.getAnswersAsString()
        )

        fileRepository.writeLine(
            date = date,
            time = time,
            ip = userIP,
            username = usersRepository.getUsernameByIp(userIP),
            answer = choice
        )
    }

    private suspend fun checkPossibleErrors(file: File, call: ApplicationCall) {
        // Si aun no se ha terminado el tiempo no se podra descargar el archivo.
        if (!questionRepository.getTimerState()) call.respondRedirect("/error/3")
        // Si nadie a contestado la pregunta no se podra descargar el archivo.
        if (!file.exists()) call.respondRedirect("/error/6")
    }

    /**
     * Reemplaza los marcadores de posición del archivo HTML por los valores correspondientes.
     *
     * @param content Contenido del archivo HTML.
     * @param question Pregunta a la que se refiere el archivo HTML.
     * @return Contenido del archivo HTML con los marcadores reemplazados.
     */
    private fun replace(content: String, question: Question): String = content
        .replace("[SEND]", appContext.getString(R.string.send_button_placeholder))
        .replace("[LOGIN_TITLE]", appContext.getString(R.string.login_title_placeholder))
        .replace("[QUESTION]", question.question)
        .replace("[MAX_ANSWER]", question.maxNumericAnswer.toString())
        .replace("[SUCCESS_MESSAGE]", appContext.getString(R.string.success_message))
        .replace("[CONFIRM_MESSAGE]", appContext.getString(R.string.confirm_message))
        .replace("[ACCESS]", appContext.getString(R.string.access_button_placeholder))
        .replace("[USER_HINT]", appContext.getString(R.string.username_text_hint))
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