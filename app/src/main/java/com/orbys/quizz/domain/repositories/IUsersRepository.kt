package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.User

interface IUsersRepository {
    // Devuelve la lista de usuarios
    fun getUsersList(): List<User>

    // Eliminar todos los usuarios de la lista
    fun clearRespondedUsers()

    // Añadir un usuario a la lista
    fun addRespondedUser(user: User)

    // Marcar al usuario como que ya ha respondido
    fun setUserResponded(ip: String)
}