package com.orbys.vote.data.repositories

import com.orbys.vote.domain.models.User
import com.orbys.vote.domain.repositories.IUsersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona el flujo de datos relacionado a los clientes que acceden al servidor http.
 *
 * @property users Un flujo mutable que contiene una lista de clientes que han accedido al servidor.
 */
class UsersRepositoryImpl private constructor(): IUsersRepository {

    private var users = MutableStateFlow(listOf<User>())

    /** Devuelve el flujo que contiene la lista de clientes */
    override fun getUsersList(): StateFlow<List<User>> = users

    /** Limpia la lista de clientes */
    override fun clearRespondedUsers() { users.tryEmit(listOf()) }

    /**
     * Devuelve el nombre de usuario a partir de la IP del cliente
     * Si el cliente no tiene nombre de usuario, se cuenta como usuario anónimo
     */
    fun getUsernameByIp(ip: String): String = users.value.find { it.ip == ip }?.username ?: "Anonymous"

    /**
     * Busca un nombre de usuario en la lista de clientes
     *
     * @param username El nombre de usuario a buscar
     * @return true si el nombre de usuario existe, false en caso contrario.
     */
    fun usernameExists(username: String) = users.value.any { it.username == username }

    /**
     *  Busca una dirección IP en la lista de clientes
     *
     * @param userIp La IP del cliente a buscar
     * @return true si el usuario no existe, false en caso contrario.
     */
    fun userNotExists(userIp: String) = !users.value.any { it.ip == userIp }

    /**
     * Comprueba si un cliente con IP dada ya ha respondido a la pregunta
     *
     * @param ip La IP del cliente a buscar
     * @return true si el cliente ha respondido, false en caso contrario.
     */
    fun userResponded(ip: String): Boolean = users.value.any { it.ip == ip && it.responded }

    /** Marca al cliente con la IP dada como que ya ha respondido */
    fun setUserResponded(ip: String) {
        users.value = users.value.map { user ->
            if (user.ip == ip) user.copy(responded = true) else user
        }
    }

    /** Añade un nuevo cliente a la lista */
    fun addUser(user: User) {
        users.value.find { it.ip == user.ip } ?: run {
            users.value += user
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UsersRepositoryImpl? = null

        /** Instancia única de la clase UserRepositoryImpl */
        fun getInstance(): UsersRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                UsersRepositoryImpl().also { INSTANCE = it }
            }
        }
    }

}