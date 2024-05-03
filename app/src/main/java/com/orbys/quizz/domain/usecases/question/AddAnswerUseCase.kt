package com.orbys.quizz.domain.usecases.question

import com.orbys.quizz.domain.repositories.IQuestionRepository
import javax.inject.Inject

// Caso de uso para añadir una nueva respuesta a una pregunta
class AddAnswerUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke(answerText: String) { repository.addAnswer(answerText) }
}