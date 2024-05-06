package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona los usuarios que han respondido las preguntas.
 *
 * @property respondedUsers Un flujo mutable privado que contiene una lista de usuarios que han accedido al servidor.
 * @property getUsersList Un flujo inmutable y publico para llamar a la lista de usuarios.
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

    private var respondedUsers = MutableStateFlow(listOf<User>())

    override fun getUsersList(): StateFlow<List<User>> = respondedUsers
    override fun clearRespondedUsers() { respondedUsers.tryEmit(listOf()) }
    override fun setUserResponded(ip: String) {
        val users = respondedUsers.value.toMutableList()
        users.find { it.ip == ip }?.responded = true
        respondedUsers.value = users.toList()
    }
    override fun addRespondedUser(user: User) {
        if (respondedUsers.value.any { it.ip == user.ip }) return

        val users = respondedUsers.value.toMutableList()
        users.add(user)
        respondedUsers.value = users.toList()
    }

}