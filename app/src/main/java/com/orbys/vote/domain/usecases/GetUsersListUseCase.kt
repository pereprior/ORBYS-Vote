package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.models.Client
import com.orbys.vote.domain.repositories.IClientRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// Caso de uso para obtener la lista de usuarios
class GetUsersListUseCase @Inject constructor(
    private val repository: IClientRepository
) {
    operator fun invoke(): StateFlow<List<Client>> = repository.getList()
}