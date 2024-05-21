package com.orbys.vote.data.controllers.handlers

import io.ktor.server.routing.Route

interface IHttpHandler {
    fun setupRoutes(route: Route)
}