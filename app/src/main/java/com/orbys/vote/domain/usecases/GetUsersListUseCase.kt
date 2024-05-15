package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.models.User
import com.orbys.vote.domain.repositories.IUsersRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// Caso de uso para obtener la lista de usuarios
class GetUsersListUseCase @Inject constructor(
    private val repository: IUsersRepository
) {
    operator fun invoke(): StateFlow<List<User>> = repository.getUsersList()
}