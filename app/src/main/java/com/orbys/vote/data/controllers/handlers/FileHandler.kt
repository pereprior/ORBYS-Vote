package com.orbys.vote.data.controllers.handlers

import android.content.Context
import com.orbys.vote.R
import com.orbys.vote.core.extensions.DOWNLOAD_ENDPOINT
import com.orbys.vote.core.extensions.ERROR_ENDPOINT
import com.orbys.vote.core.extensions.getAnswers
import com.orbys.vote.core.extensions.loadFile
import com.orbys.vote.core.extensions.loadImage
import com.orbys.vote.data.repositories.ClientRepositoryImpl
import com.orbys.vote.data.repositories.FileRepositoryImpl
import com.orbys.vote.data.repositories.QuestionRepositoryImpl
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.domain.models.Question
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.util.Locale
import javax.inject.Inject

/**
 * Clase para gestionar las operaciones relacionadas con los ficheros que maneja el servidor.
 *
 * @property appContext Contexto de la aplicación.
 */
class FileHandler @Inject constructor(private val appContext: Context) {

    private val questionRepository = QuestionRepositoryImpl.getInstance()
    private val usersRepository = ClientRepositoryImpl.getInstance()
    private val fileRepository = FileRepositoryImpl.getInstance(appContext)

    fun setupRoutes(route: Route) {
        route.apply {
            handleDownloadRoute()
            staticContent()
        }
    }

    /** Rutas GET para proporcionar a los ficheros de la p�gina web las dependencias y los estilos necesarios */
    private fun Route.staticContent() {
        // Rutas para proporcionar los archivos para el estilo de la web.
        get("/css/styles.css") { loadFile("css/styles.css") }
        get("/js/utils.js") { loadFile("js/utils.js", ContentType.Application.JavaScript) }

        // Rutas para proporcionar las imagenes a la web.
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
        get("/images/empty_radio.svg") { loadImage("empty_radio.svg") }
        get("/images/empty_checkbox.svg") { loadImage("empty_checkbox.svg") }
        get("/images/filled_radio.svg") { loadImage("filled_radio.svg") }
        get("/images/filled_checkbox.svg") { loadImage("filled_checkbox.svg") }
        get("/images/triangle.svg") { loadImage("triangle.svg") }
    }

    /** Ruta GET para que los usuarios puedan descargar un fichero con los resultados de la votación */
    private fun Route.handleDownloadRoute() = get(DOWNLOAD_ENDPOINT) {
        val file = fileRepository.resultFile

        // Si el tiempo para responder la pregunta esta activo, no se puede descargar el archivo.
        if (!questionRepository.getTimerState()) call.respondRedirect("$ERROR_ENDPOINT/3")
        // Si nadie a contestado la pregunta, no se puede descargar el archivo.
        if (!file.exists()) call.respondRedirect("$ERROR_ENDPOINT/6")

        call.respondFile(file)
    }

    /** Función para eliminar el fichero de los resultados una vez los usuarios eliminan la pregunta */
    fun deleteFile() { fileRepository.deleteFile() }

    /**
     * Carga el archivo HTML correspondiente a la pregunta lanzada.
     *
     * @param htmlName Nombre del archivo HTML.
     * @return Cadena con el contenido del archivo.
     */
    fun loadHtmlFile(htmlName: String): String? {
        val name = if (htmlName == AnswerType.YES_NO.name) "boolean" else htmlName.lowercase(Locale.ROOT)
        val fileName = name + HTTP_FILES_PLACEHOLDER + HTTP_FILES_EXTENSION

        return this::class.java.getResource("$HTTP_FILES_FOLDER/$fileName")?.readText()?.let {
            // Reemplaza los marcadores de posición del archivo HTML por los valores correspondientes.
            replaceHolders(it, questionRepository.getQuestion())
        }
    }

    /**
     * Crea el archivo para almacenar los resultados si no existe. Si existe, lo sobrescribe.
     * Crea una nueva línea en el archivo con la respuesta del usuario.
     *
     * @param choice Respuesta del usuario.
     * @param userIP IP del usuario que ha contestado.
     */
    fun createDataFile(choice: String, userIP: String) {
        val question = questionRepository.getQuestion()

        fileRepository.createFile(question)
        fileRepository.writeLine(
            ip = userIP,
            username = usersRepository.getUsernameByIp(userIP),
            answer = choice
        )
    }

    /**
     * Reemplaza los marcadores de posición del archivo HTML por los valores correspondientes.
     *
     * @param content Contenido del archivo HTML.
     * @param question Pregunta a la que se refiere el archivo HTML.
     */
    private fun replaceHolders(content: String, question: Question) = content
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

    /** Reemplaza todas las respuestas que tiene la pregunta por el valor correspondiente */
    private fun String.replaceAnswersNames(question: Question): String {
        var result = this
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
    }

}