package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IQuestionRepository
import javax.inject.Inject

/** Caso de uso para establecer el estado del temporizador de la pregunta */
class SetTimeOutUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke(isFinished: Boolean) { repository.setTimerAs(isFinished) }
}