package com.orbys.quizz.domain.repositories

import kotlinx.coroutines.flow.StateFlow

interface IUsersRepository {
    // Eliminar todos los usuarios registrados como que han respondido
    fun clearRespondedUsers()

    // Añadir un usuario a la lista de usuarios que han respondido
    fun addRespondedUser(user: String)

    // Comprobar si un usuario ya ha respondido
    fun exists(user: String): Boolean

    // Numero de usuarios que han respondido
    fun getRespondedUsersCount(): StateFlow<List<String>>
}