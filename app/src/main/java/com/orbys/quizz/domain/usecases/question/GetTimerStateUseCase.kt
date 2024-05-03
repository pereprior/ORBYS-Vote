package com.orbys.quizz.domain.usecases.question

import com.orbys.quizz.domain.repositories.IQuestionRepository
import javax.inject.Inject

// Caso de uso para obtener el estado del temporizador
class GetTimerStateUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke() = repository.getTimerState()
}