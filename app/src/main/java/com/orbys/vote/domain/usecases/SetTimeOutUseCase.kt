package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IQuestionRepository
import javax.inject.Inject

class SetTimeOutUseCase @Inject constructor(
    private val repository: IQuestionRepository
) {
    operator fun invoke(isFinished: Boolean) { repository.setTimeOut(isFinished) }
}