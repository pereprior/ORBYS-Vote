package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.data.models.Answer
import kotlinx.coroutines.flow.Flow
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

    fun getAnswer(): Flow<Answer> {
        return this.repository.answer
    }

}