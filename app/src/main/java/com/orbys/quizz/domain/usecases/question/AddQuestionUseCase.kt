package com.orbys.quizz.domain.usecases.question

import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.domain.repositories.IQuestionRepository
import javax.inject.Inject

// Caso de uso para crear una nueva pregunta
class AddQuestionUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke(question: Question) { repository.addQuestion(question) }
}