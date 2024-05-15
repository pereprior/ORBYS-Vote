package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IQuestionRepository
import javax.inject.Inject

// Caso de uso para obtener la informacion de la pregunta
class GetQuestionUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke() = repository.getQuestion()
}