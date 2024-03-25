package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.data.models.Answer
import com.pprior.quizz.domain.viewModels.QuestionViewModel
import org.koin.java.KoinJavaComponent

class QuestionRepositoryImp: IQuestionRepository {

    private val viewModel: QuestionViewModel by KoinJavaComponent.inject(QuestionViewModel::class.java)

    override fun setPostInAnswerCount(answer: String?) {
        if (answer == "Si") {
            this.viewModel.incYesAnswer()
        }

        if (answer == "No") {
            this.viewModel.incNoAnswer()
        }
    }

    fun getAnswer(): Answer {
        return this.viewModel.getAnswer()
    }

}