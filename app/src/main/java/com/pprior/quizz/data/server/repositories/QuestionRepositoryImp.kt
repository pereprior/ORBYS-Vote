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
            this.repository.incYesAnswer()
        }

        if (answer == "No") {
            this.repository.incNoAnswer()
        }
    }

    override fun addUserToRespondedList(userIp: String) = repository.addRespondedUser(userIp)
    fun userNotExists(userIp: String): Boolean {
        return !repository.exists(userIp)
    }

}