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
    fun getQuestion() = questionRepository.question.value
    fun setUserResponded(ip: String) = usersRepository.setUserResponded(ip)


    // Incrementa el contador de respuestas
    override fun setPostInAnswerCount(answer: String?) { questionRepository.incAnswer(answer ?: "") }
    // Obtiene un objeto pregunta del repositorio a partir del texto de la pregunta
    //override fun findQuestion(questionID: String) = questionRepository.findQuestion(questionID.toInt())
    // Añade un usuario a la lista de usuarios que han respondido
    override fun addUserToRespondedList(user: User) = usersRepository.addRespondedUser(user)
    // Comprueba si un usuario ya ha respondido
    override fun userNotExists(userIp: String) = !usersRepository.exists(userIp)
    override fun userResponded(ip: String): Boolean = usersRepository.usersResponded(ip)
}