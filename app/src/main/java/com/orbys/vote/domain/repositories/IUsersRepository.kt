package com.orbys.vote.domain.repositories

import com.orbys.vote.domain.models.User
import kotlinx.coroutines.flow.StateFlow

interface IUsersRepository {
    // Devuelve la lista de usuarios
    fun getUsersList(): StateFlow<List<User>>

    // Eliminar todos los usuarios de la lista
    fun clearRespondedUsers()
}