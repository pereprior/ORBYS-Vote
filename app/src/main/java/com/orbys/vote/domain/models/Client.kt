package com.orbys.vote.domain.models

/**
 * Clase que representa un cliente que accede al servidor para contestar una pregunta.
 *
 * @param ip Dirección IP del cliente.
 * @param username Nombre de usuario del cliente (por defecto es anónimo).
 * @param responded Indica si el cliente ha contestado la pregunta (true cuando el cliente ya no pueda contestar).
 */
data class Client(
    val ip: String,
    val username: String = "Anonymous",
    var responded: Boolean = false
)