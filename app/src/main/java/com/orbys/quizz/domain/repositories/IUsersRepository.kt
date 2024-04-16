package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.StateFlow

interface IUsersRepository {
    // Eliminar todos los usuarios de la lista
    fun clearRespondedUsers()

    // Añadir un usuario a la lista
    fun addRespondedUser(user: User)

    // Comprobar si un usuario ya esta en la lista
    fun exists(ip: String): Boolean

    // Comprobar si un usuario ya ha respondido
    fun usersResponded(ip: String): Boolean

    // Marcar al usuario como que ya ha respondido
    fun setUserResponded(ip: String)
}