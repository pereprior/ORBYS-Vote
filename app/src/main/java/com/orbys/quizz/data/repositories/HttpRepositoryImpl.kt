package com.orbys.quizz.data.repositories

import com.orbys.quizz.core.extensions.checkIfAnswerExists
import com.orbys.quizz.domain.models.User
import com.orbys.quizz.domain.usecases.question.AddAnswerUseCase
import com.orbys.quizz.domain.usecases.question.GetQuestionUseCase
import com.orbys.quizz.domain.usecases.question.GetTimerStateUseCase
import com.orbys.quizz.domain.usecases.question.IncAnswerUseCase
import com.orbys.quizz.domain.usecases.users.AddRespondedUserUseCase
import com.orbys.quizz.domain.usecases.users.GetRespondedUsersUseCase
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
    private val getUsers: GetRespondedUsersUseCase,
    private val addUser: AddRespondedUserUseCase,
    private val setUserResponded: SetUserRespondedUseCase
): IHttpRepository {
    // Obtiene el estado del temporizador
    override fun timerState() = getTimerState()

    // Obtiene la pregunta que lanza el servidor
    override fun getQuestionInfo() = getQuestion()

    // Incrementa el contador de respuestas de la pregunta
    override fun setPostInAnswerCount(answer: String?) {

        // Si el usuario ha enviado varias respuestas
        if (answer?.contains(",") == true) {
            val answers = answer.split(",")

            answers.forEach { incAnswer(it) }

            // Si el usuario ha enviado una sola respuesta
        } else incAnswer(answer ?: "")
    }

    override fun addAnswerToList(answer: String?) = if (answer is String) addAnswer(answer) else Unit

    fun answerExists(answer: String?) = getQuestion().checkIfAnswerExists(answer ?: "")

    // Marca a un usuario como que ha respondido
    override fun setUserAsResponded(ip: String) = setUserResponded(ip)

    // Añade un usuario a la lista
    override fun addUserToRespondedList(user: User) = addUser(user)

    // Comprueba si un usuario ya existe en el repositorio
    override fun userNotExists(userIp: String) = !getUsers().any { it.ip == userIp }

    // Comprueba si ya existe un usuario con el mismo nombre
    fun usernameExists(username: String) = getUsers().any { it.username == username }

    // Comprueba si un usuario ya ha respondido
    override fun userResponded(ip: String): Boolean = getUsers().any { it.ip == ip && it.responded }

    // Obtiene el nombre de usuario a partir de la IP
    override fun getUsernameByIp(ip: String): String = getUsers().find { it.ip == ip }?.username ?: "Anonymous"
}