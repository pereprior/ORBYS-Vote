package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.data.flow.FlowRepository
import org.koin.java.KoinJavaComponent.inject

/**
 * Implementación del repositorio de preguntas.
 *
 * Esta clase se encarga de gestionar las operaciones relacionadas con las respuestas.
 */
class QuestionRepositoryImp: IQuestionRepository {

    private val repository: FlowRepository by inject(FlowRepository::class.java)

    override fun setPostInAnswerCount(answer: String?) {
        if (answer == "Si") {
            this.repository.incAnswer()
        }

        if (answer == "No") {
            this.repository.incAnswer()
        }
    }

    fun findQuestion(question: String) = repository.findQuestionByText(question)

    override fun addUserToRespondedList(userIp: String) = repository.addRespondedUser(userIp)

}