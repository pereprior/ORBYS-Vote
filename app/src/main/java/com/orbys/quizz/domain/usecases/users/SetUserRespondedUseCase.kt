package com.orbys.quizz.domain.usecases.users

import com.orbys.quizz.domain.repositories.IUsersRepository
import javax.inject.Inject

class SetUserRespondedUseCase @Inject constructor(
    private val repository: IUsersRepository
) {
    operator fun invoke(ip: String) { repository.setUserResponded(ip) }
}