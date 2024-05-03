package com.orbys.quizz.domain.usecases.users

import com.orbys.quizz.domain.repositories.IUsersRepository
import javax.inject.Inject

// Caso de uso para obtener la lista de usuarios
class GetUsersListUseCase @Inject constructor(
    private val repository: IUsersRepository
) {
    operator fun invoke() = repository.getUsersList()
}