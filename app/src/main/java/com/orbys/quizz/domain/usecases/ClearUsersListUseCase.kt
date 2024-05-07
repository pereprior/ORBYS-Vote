package com.orbys.quizz.domain.usecases

import com.orbys.quizz.domain.repositories.IUsersRepository
import javax.inject.Inject

class ClearUsersListUseCase @Inject constructor(
    private val usersRepository: IUsersRepository
){
    operator fun invoke() = usersRepository.clearRespondedUsers()
}