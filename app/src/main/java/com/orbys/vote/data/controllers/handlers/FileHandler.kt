package com.orbys.vote.data.controllers.handlers

import android.content.Context
import com.orbys.vote.R
import com.orbys.vote.core.extensions.getAnswerType
import com.orbys.vote.core.extensions.getAnswers
import com.orbys.vote.core.extensions.getAnswersAsString
import com.orbys.vote.core.managers.NetworkManager.Companion.DOWNLOAD_ENDPOINT
import com.orbys.vote.data.repositories.FileRepository
import com.orbys.vote.data.repositories.QuestionRepositoryImpl
import com.orbys.vote.data.repositories.UsersRepositoryImpl
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respondBytes
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.util.pipeline.PipelineContext
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

    fun setupRoutes(route: Route) {

        route.apply {
            handleDownloadRoute()
            staticContent()
        }

    }

    /**
     * Ruta para obtener los archivos almacenados en la carpeta de assets.
     *
     * @return GET
     */
    private fun Route.staticContent() {
        // Rutas para obtener los archivos para el estilo de la web.
        get("/css/styles.css") {
            try {
                val css = this::class.java.classLoader!!.getResource("assets/css/styles.css")!!.readText()
                call.respondText(css, ContentType.Text.CSS)
            } catch (e: Exception) {
                call.respondRedirect("/error/1")
            }
        }

        // Rutas para obtener proporcionar las imagenes a la web.
        get("/images/background.svg") { loadImage("background.svg") }
        get("/images/vote.svg") { loadImage("vote.svg") }
        get("/images/orbys.svg") { loadImage("orbys.svg") }
        get("/images/boolean.svg") { loadImage("boolean.svg") }
        get("/images/numeric.svg") { loadImage("numeric.svg") }
        get("/images/others.svg") { loadImage("others.svg") }
        get("/images/stars.svg") { loadImage("stars.svg") }
        get("/images/empty_star.svg") { loadImage("empty_star.svg") }
        get("/images/filled_star.svg") { loadImage("filled_star.svg") }
        get("/images/error.svg") { loadImage("error.svg") }
        get("/images/timeout.svg") { loadImage("timeout.svg") }
        get("/images/alert.svg") { loadImage("alert.svg") }
        get("/images/success.svg") { loadImage("success.svg") }
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
        val name = if (htmlName == AnswerType.YES_NO.name) "boolean" else htmlName.lowercase(Locale.ROOT)
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
        val question = questionRepository.getQuestion()

        fileRepository.createFile(
            fileName = CSV_FILE_NAME,
            question = question,
            answers = question.getAnswersAsString()
        )

        fileRepository.writeLine(
            date = date,
            time = time,
            ip = userIP,
            username = usersRepository.getUsernameByIp(userIP),
            answer = choice
        )
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.loadImage(
        imagePath: String, contentType: ContentType = ContentType.Image.SVG
    ) {
        try {
            // Almacenamos la imagen en la cache del navegador durante 24 horas.
            call.response.headers.append("Cache-Control", "max-age=86400")

            val image = this::class.java.classLoader!!.getResource("assets/images/$imagePath").readBytes()
            call.respondBytes(image, contentType)
        } catch (e: Exception) {
            call.respondRedirect("/error/1")
        }
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
        .replace("[USER_ALREADY_EXISTS]", appContext.getString(R.string.user_already_exists_message))
        .replace("[ACCESS]", appContext.getString(R.string.access_button_placeholder))
        .replace("[USER_HINT]", appContext.getString(R.string.username_text_hint))
        .replace("[CLOSE]", appContext.getString(R.string.close_button_placeholder))
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
    }

    private companion object {
        const val HTTP_FILES_FOLDER = "/assets/templates"
        const val HTTP_FILES_PLACEHOLDER = "_index"
        const val HTTP_FILES_EXTENSION = ".html"
        const val MULTIPLE_CHOICE = "multiple"
        const val SINGLE_CHOICE = "single"
        const val CSV_FILE_NAME = "data"
        const val TIME_FORMAT = "HH:mm:ss"
        const val DATE_FORMAT = "dd/MM/yyyy"
    }

}