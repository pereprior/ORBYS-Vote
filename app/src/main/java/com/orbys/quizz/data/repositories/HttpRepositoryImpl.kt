package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.User
import com.orbys.quizz.domain.repositories.QuestionRepositoryImpl
import com.orbys.quizz.domain.repositories.UsersRepositoryImpl
import javax.inject.Inject

/**
 * Implementación del repositorio de preguntas.
 *
 * Esta clase se encarga de gestionar las operaciones relacionadas con las respuestas.
 */
class HttpRepositoryImpl @Inject constructor(
    private val questionRepository: QuestionRepositoryImpl,
    private val usersRepository: UsersRepositoryImpl
): IHttpRepository {
    // Obtiene el estado del temporizador
    override fun timeOut() = questionRepository.timeOut

    // Obtiene la pregunta que lanza el servidor
    override fun getQuestion() = questionRepository.question.value

    // Incrementa el contador de respuestas de la pregunta
    override fun setPostInAnswerCount(answer: String?) {

        // Si el usuario ha enviado varias respuestas
        if (answer?.contains(",") == true) {
            val answers = answer.split(",")

            answers.forEach { questionRepository.incAnswer(it) }

            // Si el usuario ha enviado una sola respuesta
        } else questionRepository.incAnswer(answer ?: "")
    }

    override fun addAnswer(answer: String?) = if (answer is String) questionRepository.addAnswer(answer) else Unit

    fun answerExists(answer: String?) = questionRepository.question.value.answers.value.any { it.answer == answer }

    // Marca a un usuario como que ha respondido
    override fun setUserResponded(ip: String) = usersRepository.setUserResponded(ip)

    // Añade un usuario a la lista
    override fun addUser(user: User) = usersRepository.addRespondedUser(user)

    // Comprueba si un usuario ya existe en el repositorio
    override fun userNotExists(userIp: String) = !usersRepository.exists(userIp)

    // Comprueba si un usuario ya ha respondido
    override fun userResponded(ip: String): Boolean = usersRepository.usersResponded(ip)

    // Obtiene el nombre de usuario a partir de la IP
    override fun getUsernameByIp(ip: String): String = usersRepository.getUsernameByIp(ip)
}