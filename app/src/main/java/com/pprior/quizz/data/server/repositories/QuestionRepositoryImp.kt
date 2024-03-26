package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.data.flow.FlowRepository
import kotlinx.coroutines.flow.toList
import org.koin.java.KoinJavaComponent.inject

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