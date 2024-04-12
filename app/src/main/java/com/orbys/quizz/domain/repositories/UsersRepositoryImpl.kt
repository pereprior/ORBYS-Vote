package com.orbys.quizz.domain.repositories

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
    private var respondedUsers = MutableStateFlow(listOf<String>())

    override fun exists(user: String) = respondedUsers.replayCache.firstOrNull()?.contains(user) ?: false
    override fun clearRespondedUsers() { respondedUsers.tryEmit(listOf()) }
    override fun getRespondedUsersCount() = respondedUsers
    override fun addRespondedUser(user: String) {
        val users = respondedUsers.value.toMutableList()
        users.add(user)
        respondedUsers.value = users.toList()
    }

}