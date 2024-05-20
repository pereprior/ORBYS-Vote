package com.orbys.vote.data.controllers.handlers

import android.content.Context
import com.orbys.vote.R
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import javax.inject.Inject

/**
 * Clase para gestionar los errores y excepciones del servidor http.
 *
 * @param appContext Contexto de la aplicación.
 */
class ErrorHandler @Inject constructor(
    private val appContext: Context
) {

    enum class ErrorType(val code: Int) {
        ERROR(0),
        FILE_NOT_FOUND(1),
        USER_ALREADY_EXISTS(2),
        TIME_IN(3),
        TIME_OUT(5),
        NO_RESPONSE(6),
        USER_RESPONDED(7)
    }

    fun setupRoutes(route: Route, fileHandler: FileHandler) {

        route.apply {
            handleError(fileHandler)
        }

    }

    /**
     * Ruta con la página con el error correspondiente.
     *
     * @param fileHandler Gestor de archivos.
     * @return GET
     */
    private fun Route.handleError(
        fileHandler: FileHandler
    ) = get("$ERROR_ENDPOINT/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: 0
        val errorType = getErrorById(id)
        val fileContent = replacePopupPlaceHolders(fileHandler.loadHtmlFile("error"), errorType)

        call.respondText(
            text = fileContent ?: appContext.getString(R.string.file_not_found_message),
            contentType = ContentType.Text.Html
        )
    }

    /**
     * Reemplaza el mensaje del popup con el error correspondiente.
     *
     * @param content Contenido del archivo html.
     * @param errorType Tipo de error que llama.
     * @return Contenido del archivo html con los marcadores reemplazados.
     */
    private fun replacePopupPlaceHolders(content: String?, errorType: ErrorType): String? {

        val errorMessage = when (errorType) {

            ErrorType.USER_ALREADY_EXISTS -> appContext.getString(R.string.user_already_exists_message)

            ErrorType.FILE_NOT_FOUND -> appContext.getString(R.string.file_not_found_message)

            ErrorType.TIME_IN -> appContext.getString(R.string.time_in_message)

            ErrorType.TIME_OUT -> appContext.getString(R.string.time_out_message)

            ErrorType.USER_RESPONDED -> appContext.getString(R.string.user_responded_message)

            ErrorType.NO_RESPONSE -> appContext.getString(R.string.no_response_message)

            else -> "ERROR"
        }

        return content
            ?.replace(POPUP_CONTENT_PLACEHOLDER, errorMessage)
            ?.replace("[ERROR_TYPE]", errorType.code.toString())
    }

    /**
     * Obtiene el tipo de error a partir de su codigo.
     *
     * @param id Identificador del error.
     * @return Tipo de error correspondiente
     */
    private fun getErrorById(id: Int) = when (id) {

        1 -> ErrorType.FILE_NOT_FOUND

        2 -> ErrorType.USER_ALREADY_EXISTS

        3 -> ErrorType.TIME_IN

        5 -> ErrorType.TIME_OUT

        6 -> ErrorType.NO_RESPONSE

        7 -> ErrorType.USER_RESPONDED

        else -> ErrorType.ERROR

    }

    private companion object {
        const val POPUP_CONTENT_PLACEHOLDER = "[POPUP_CONTENT]"
        const val ERROR_ENDPOINT = "/error"
    }

}