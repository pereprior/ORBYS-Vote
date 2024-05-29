package com.orbys.vote.data.repositories

import com.orbys.vote.domain.models.Client
import com.orbys.vote.domain.repositories.IClientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Clase que gestiona el flujo de datos relacionado a los clientes que acceden al servidor http.
 *
 * @property clientList Un flujo mutable que contiene una lista de clientes que han accedido al servidor.
 */
class ClientRepositoryImpl private constructor(): IClientRepository {

    private var clientList = MutableStateFlow(listOf<Client>())

    /** Devuelve el flujo que contiene la lista de clientes */
    override fun getList(): StateFlow<List<Client>> = clientList

    /** Limpia la lista de clientes */
    override fun clearList() { clientList.tryEmit(listOf()) }

    /**
     * Devuelve el nombre de usuario a partir de la IP del cliente
     * Si el cliente no tiene nombre de usuario, se cuenta como usuario anónimo
     */
    fun getUsernameByIp(ip: String): String = clientList.value.find { it.ip == ip }?.username ?: "Anonymous"

    /**
     * Busca un nombre de usuario en la lista de clientes
     *
     * @param username El nombre de usuario a buscar
     * @return true si el nombre de usuario existe, false en caso contrario.
     */
    fun usernameExists(username: String) = clientList.value.any { it.username == username }

    /**
     *  Busca una dirección IP en la lista de clientes
     *
     * @param userIp La IP del cliente a buscar
     * @return true si el usuario no existe, false en caso contrario.
     */
    fun clientNotExists(userIp: String) = !clientList.value.any { it.ip == userIp }

    /**
     * Comprueba si un cliente con IP dada ya ha respondido a la pregunta
     *
     * @param ip La IP del cliente a buscar
     * @return true si el cliente ha respondido, false en caso contrario.
     */
    fun clientResponded(ip: String): Boolean = clientList.value.any { it.ip == ip && it.responded }

    /** Marca al cliente con la IP dada como que ya ha respondido */
    fun setClientResponded(ip: String) {
        clientList.value = clientList.value.map { user ->
            if (user.ip == ip) user.copy(responded = true) else user
        }
    }

    /** Añade un nuevo cliente a la lista */
    fun addClient(client: Client) {
        clientList.value.find { it.ip == client.ip } ?: run {
            clientList.value += client
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ClientRepositoryImpl? = null

        /** Instancia única de la clase UserRepositoryImpl */
        fun getInstance(): ClientRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                ClientRepositoryImpl().also { INSTANCE = it }
            }
        }
    }

}