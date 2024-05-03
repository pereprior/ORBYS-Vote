package com.orbys.quizz.domain.usecases.question

import com.orbys.quizz.domain.repositories.IQuestionRepository
import javax.inject.Inject

// Caso de uso para incrementar el contador de respuestas
class IncAnswerUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke(answer: String) { repository.incAnswer(answer) }
}