package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IUsersRepository
import javax.inject.Inject

class ClearUsersListUseCase @Inject constructor(
    private val usersRepository: IUsersRepository
){
    operator fun invoke() = usersRepository.clearRespondedUsers()
}