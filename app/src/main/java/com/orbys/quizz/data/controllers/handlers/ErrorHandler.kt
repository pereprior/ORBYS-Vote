package com.orbys.quizz.data.controllers.handlers

import android.content.Context
import com.orbys.quizz.R
import io.ktor.http.ContentType
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import javax.inject.Inject

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

    private companion object {
        const val POPUP_CONTENT_PLACEHOLDER = "[POPUP_CONTENT]"
    }

    fun setupRoutes(route: Route, fileHandler: FileHandler) {
        route.handleError(fileHandler)
    }

    private fun Route.handleError(fileHandler: FileHandler) = get("/error/{id}") {
        val id = call.parameters["id"]?.toIntOrNull() ?: 0
        val errorType = getErrorById(id)
        val fileContent = replacePopupPlaceHolders(fileHandler.loadHtmlFile("error"), errorType)

        call.respondText(
            text = fileContent ?: appContext.getString(R.string.file_not_found_message),
            contentType = ContentType.Text.Html
        )
    }

    private fun replacePopupPlaceHolders(content: String?, errorType: ErrorType): String? {

        val errorMessage = when (errorType) {

            ErrorType.USER_ALREADY_EXISTS -> appContext.getString(R.string.user_already_exists_message)

            ErrorType.FILE_NOT_FOUND -> appContext.getString(R.string.file_not_found_message)

            ErrorType.TIME_IN -> appContext.getString(R.string.time_in_message)

            ErrorType.TIME_OUT -> appContext.getString(R.string.time_out_message)

            ErrorType.USER_RESPONDED -> appContext.getString(R.string.user_responded_message)

            ErrorType.NO_RESPONSE -> appContext.getString(R.string.no_response_message)

            else -> null
        }

        return content?.replace(POPUP_CONTENT_PLACEHOLDER, errorMessage ?: "")
    }

    private fun getErrorById(id: Int) = when (id) {

        1 -> ErrorType.FILE_NOT_FOUND

        2 -> ErrorType.USER_ALREADY_EXISTS

        3 -> ErrorType.TIME_IN

        5 -> ErrorType.TIME_OUT

        6 -> ErrorType.NO_RESPONSE

        7 -> ErrorType.USER_RESPONDED

        else -> ErrorType.ERROR

    }

}