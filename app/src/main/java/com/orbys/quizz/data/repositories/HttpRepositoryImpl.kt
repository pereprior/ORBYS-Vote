package com.orbys.quizz.data.repositories

import com.orbys.quizz.core.extensions.getAnswers
import com.orbys.quizz.domain.models.User
import com.orbys.quizz.domain.usecases.question.AddAnswerUseCase
import com.orbys.quizz.domain.usecases.question.GetQuestionUseCase
import com.orbys.quizz.domain.usecases.question.GetTimerStateUseCase
import com.orbys.quizz.domain.usecases.question.IncAnswerUseCase
import com.orbys.quizz.domain.usecases.users.AddUserUseCase
import com.orbys.quizz.domain.usecases.users.GetUsersListUseCase
import com.orbys.quizz.domain.usecases.users.SetUserRespondedUseCase
import javax.inject.Inject

/**
 * Implementación del repositorio de preguntas.
 *
 * Esta clase se encarga de gestionar las operaciones relacionadas con las respuestas.
 */
class HttpRepositoryImpl @Inject constructor(
    private val getQuestion: GetQuestionUseCase,
    private val incAnswer: IncAnswerUseCase,
    private val addAnswer: AddAnswerUseCase,
    private val getTimerState: GetTimerStateUseCase,
    private val getUsers: GetUsersListUseCase,
    private val addUser: AddUserUseCase,
    private val setUserResponded: SetUserRespondedUseCase
): IHttpRepository {
    // Devuelve true si el temporizador ha finalizado
    override fun isTimeOut() = getTimerState().value

    // Obtiene la pregunta que lanza el servidor
    override fun getQuestionInfo() = getQuestion()

    // Incrementa el contador de las respuestas
    override fun incAnswerCount(answer: String?) {

        // Si el usuario ha enviado varias respuestas
        if (answer?.contains(";") == true) {
            val answers = answer.split(";")

            answers.forEach { incAnswer(it) }

            // Si el usuario ha enviado una sola respuesta
        } else incAnswer(answer ?: "")
    }

    // Marca un usuario como que ha respondido
    override fun setUserAsResponded(ip: String) = setUserResponded(ip)

    // Añade un usuario a la lista
    override fun addUserToList(user: User) = addUser(user)

    // Añade una respuesta a la pregunta
    override fun addAnswerToList(answer: String?) = if (answer is String) addAnswer(answer) else Unit

    // Comprueba si una respuesta ya existe
    override fun answerExists(answer: String?) = getQuestion().getAnswers().any { it.answer == answer }

    // Devuelve las respuestas de la pregunta como una lista de strings
    override fun getAnswersAsString() = getQuestion().getAnswers().map { it.answer }

    // Comprueba si un usuario ya existe en la lista
    override fun userNotExists(userIp: String) = !getUsers().any { it.ip == userIp }

    // Comprueba si existe un usuario con el mismo nombre
    override fun usernameExists(username: String) = getUsers().any { it.username == username }

    // Comprueba si un usuario ya ha respondido
    override fun userResponded(ip: String): Boolean = getUsers().any { it.ip == ip && it.responded }

    // Devuelve el nombre de usuario a partir de la IP
    override fun getUsernameByIp(ip: String): String = getUsers().find { it.ip == ip }?.username ?: "Anonymous"
}