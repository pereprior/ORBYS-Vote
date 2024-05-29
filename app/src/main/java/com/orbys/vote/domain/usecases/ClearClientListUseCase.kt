package com.orbys.vote.domain.usecases

import com.orbys.vote.domain.repositories.IClientRepository
import javax.inject.Inject

/** Caso de uso para eliminar la lista de clientes que han accedido al servidor */
class ClearClientListUseCase @Inject constructor(
    private val usersRepository: IClientRepository
){
    operator fun invoke() = usersRepository.clearList()
}