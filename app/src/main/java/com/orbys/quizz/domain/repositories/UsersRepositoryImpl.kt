package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que gestiona los usuarios que han respondido las preguntas.
 *
 * @property respondedUsers Un flujo mutable que contiene una lista de usuarios que han respondido.
 */
class UsersRepositoryImpl private constructor(): IUsersRepository {
    companion object {
        // Variable para almacenar la única instancia del repositorio
        @Volatile
        private var INSTANCE: UsersRepositoryImpl? = null

        /**
         * El repositorio debe tener una unica instancia para todos los componentes de la aplicación
         * @return La única instancia de la clase UsersRepositoryImpl.
         */
        fun getInstance(): UsersRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                UsersRepositoryImpl().also { INSTANCE = it }
            }
        }
    }

    // Flujo de usuarios que han respondido
    private var respondedUsers = MutableStateFlow(listOf<User>())

    override fun exists(ip: String) = respondedUsers.value.any { it.ip == ip }
    fun usersResponded(ip: String): Boolean = respondedUsers.value.any { it.ip == ip && it.responded }
    override fun clearRespondedUsers() { respondedUsers.tryEmit(listOf()) }
    override fun getRespondedUsersCount() = respondedUsers
    fun setUserResponded(ip: String) {
        val users = respondedUsers.value.toMutableList()
        users.find { it.ip == ip }?.responded = true
        respondedUsers.value = users.toList()
    }
    override fun addRespondedUser(user: User) {
        if (exists(user.ip)) return

        val users = respondedUsers.value.toMutableList()
        users.add(user)
        respondedUsers.value = users.toList()
    }

}