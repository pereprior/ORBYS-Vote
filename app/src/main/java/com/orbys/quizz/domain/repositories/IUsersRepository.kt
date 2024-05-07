package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.StateFlow

interface IUsersRepository {
    // Devuelve la lista de usuarios
    fun getUsersList(): StateFlow<List<User>>

    // Eliminar todos los usuarios de la lista
    fun clearRespondedUsers()
}