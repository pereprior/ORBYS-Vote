package com.orbys.quizz.domain.repositories

import com.orbys.quizz.domain.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona los usuarios que han respondido las preguntas.
 *
 * @property _respondedUsers Un flujo mutable privado que contiene una lista de usuarios que han accedido al servidor.
 * @property respondedUsers Un flujo inmutable y publico para llamar a la lista de usuarios.
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

    private var _respondedUsers = MutableStateFlow(listOf<User>())
    val respondedUsers: StateFlow<List<User>> = _respondedUsers

    override fun exists(ip: String) = _respondedUsers.value.any { it.ip == ip }
    override fun usersResponded(ip: String): Boolean = _respondedUsers.value.any { it.ip == ip && it.responded }
    override fun clearRespondedUsers() { _respondedUsers.tryEmit(listOf()) }
    override fun getUsernameByIp(ip: String): String = _respondedUsers.value.find { it.ip == ip }?.username ?: "Anonymous"
    override fun setUserResponded(ip: String) {
        val users = _respondedUsers.value.toMutableList()
        users.find { it.ip == ip }?.responded = true
        _respondedUsers.value = users.toList()
    }
    override fun addRespondedUser(user: User) {
        if (exists(user.ip)) return

        val users = _respondedUsers.value.toMutableList()
        users.add(user)
        _respondedUsers.value = users.toList()
    }

}