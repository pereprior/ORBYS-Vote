package com.orbys.vote.domain.repositories

import com.orbys.vote.domain.models.Client
import kotlinx.coroutines.flow.StateFlow

/** Inversión de dependencias para el repositorio que gestiona las operaciones con los clientes del servidor */
interface IClientRepository {
    fun getList(): StateFlow<List<Client>>
    fun clearList()
}