package com.orbys.quizz.data.repositories

import com.orbys.quizz.domain.models.User
import com.orbys.quizz.domain.repositories.IUsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona los usuarios que han respondido las preguntas.
 *
 * @property users Un flujo mutable privado que contiene una lista de usuarios que han accedido al servidor.
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

    private var users = MutableStateFlow(listOf<User>())

    override fun getUsersList(): StateFlow<List<User>> = users
    override fun clearRespondedUsers() { users.tryEmit(listOf()) }

    // Devuelve el nombre de usuario a partir de la IP
    fun getUsernameByIp(ip: String): String = users.value.find { it.ip == ip }?.username ?: "Anonymous"

    // Devuelve si el nombre de usuario existe en la lista de usuarios
    fun usernameExists(username: String) = users.value.any { it.username == username }

    // Devuelve si el usuario no existe en la lista de usuarios
    fun userNotExists(userIp: String) = !users.value.any { it.ip == userIp }

    // Devuelve si el usuario ha respondido
    fun userResponded(ip: String): Boolean = users.value.any { it.ip == ip && it.responded }

    // Establece que el usuario ha respondido
    fun setUserResponded(ip: String) {
        users.value = users.value.map { user ->
            if (user.ip == ip) user.copy(responded = true) else user
        }
    }

    // Añade un usuario a la lista de usuarios
    fun addUser(user: User) {
        users.value.find { it.ip == user.ip } ?: run {
            users.value += user
        }
    }

}