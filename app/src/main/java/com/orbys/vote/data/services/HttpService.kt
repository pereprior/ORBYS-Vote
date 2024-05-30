package com.orbys.vote.data.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orbys.vote.core.extensions.SERVER_PORT
import com.orbys.vote.data.controllers.HttpController
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import javax.inject.Inject

/**
 * Servicio que se encarga de iniciar el servidor HTTP
 *
 * @property controller Controlador de las rutas del servidor
 * @property server Servidor HTTP
 */
@AndroidEntryPoint
class HttpService: Service() {

    @Inject
    lateinit var controller: HttpController
    private var server: ApplicationEngine? = null

    override fun onCreate() {
        super.onCreate()

        startServer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_NOT_STICKY

    /** Inicia el servidor HTTP en un hilo aparte */
    private fun startServer() {
        Thread {
            server = createServer()
            server?.start(wait = false)
        }.start()
    }

    /** Crea el servidor HTTP con las plantillas y las rutas correspondientes */
    private fun createServer() = embeddedServer(Netty, port = SERVER_PORT) {
        routing { controller.setupRoutes(this) }
    }

    override fun onDestroy() {
        // Elimina el fichero de los resultados
        controller.clearDataFile()
        // Detiene el servidor HTTP
        server?.stop(1000, 5000)

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}