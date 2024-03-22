package com.pprior.quizz.data.server.repositories

import android.content.res.Resources.NotFoundException
import com.pprior.quizz.domain.models.Question

class QuestionRepositoryImp (
    private val questions: List<Question>
): IQuestionRepository {

    override fun getQuestionByTitle(title: String): Question {
        questions.find {
            it.title == title
            // Si el titulo existe, devuelve la pregunta
        }?.let {
            return it
            // Si el titulo no existe, lanza una excepción
        } ?: run {
            throw NotFoundException("Question not found")
        }
    }

}