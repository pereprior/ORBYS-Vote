package com.orbys.quizz.domain.usecases

import com.orbys.quizz.domain.repositories.IQuestionRepository
import javax.inject.Inject

class TimeOutUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke() { repository.timeOut() }
}