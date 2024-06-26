package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.models.Question
import com.orbys.vote.domain.repositories.IQuestionRepository
import javax.inject.Inject

/** Caso de uso para generar una nueva pregunta */
class GenerateQuestionUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke(question: Question) { repository.generateQuestion(question) }
}