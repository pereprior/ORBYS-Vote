package com.pprior.quizz.data.server.repositories

import com.pprior.quizz.data.models.Answer
import com.pprior.quizz.ui.viewmodels.QuestionViewModel

class QuestionRepositoryImp (
    private val viewModel: QuestionViewModel
): IQuestionRepository {

    override fun setPostInAnswerCount(answer: String?) {
        if (answer == "Si") {
            this.viewModel.setYesAnswer()
        }

        if (answer == "No") {
            this.viewModel.setNoAnswer()
        }
    }

    fun getAnswer(): Answer {
        return this.viewModel.getAnswer()
    }

}