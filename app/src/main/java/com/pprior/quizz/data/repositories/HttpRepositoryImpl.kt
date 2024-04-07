package com.pprior.quizz.data.repositories

import com.pprior.quizz.domain.repositories.QuestionRepositoryImpl
import com.pprior.quizz.domain.repositories.UsersRepositoryImpl
import com.pprior.quizz.domain.models.Question
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
    // Incrementa el contador de respuestas
    override fun setPostInAnswerCount(question: Question, answer: String?) { questionRepository.incAnswer(question, answer ?: "") }
    // Obtiene un objeto pregunta del repositorio a partir del texto de la pregunta
    override fun findQuestion(question: String) = questionRepository.findQuestion(question)
    // Añade un usuario a la lista de usuarios que han respondido
    override fun addUserToRespondedList(userIp: String) = usersRepository.addRespondedUser(userIp)
    // Comprueba si un usuario ya ha respondido
    override fun userNotExists(userIp: String) = !usersRepository.exists(userIp)
}