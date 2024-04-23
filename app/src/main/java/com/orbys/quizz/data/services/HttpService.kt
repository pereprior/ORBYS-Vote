package com.orbys.quizz.data.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orbys.quizz.data.controllers.HttpController
import com.orbys.quizz.data.utils.ServerUtils.Companion.SERVER_PORT
import dagger.hilt.android.AndroidEntryPoint
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import javax.inject.Inject

/**
 * Servicio que inicia el servidor HTTP.
 *
 * @property controller Controlador de las rutas del servidor HTTP.
 * @property server El servidor HTTP que se inicia en este servicio.
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

    private fun startServer() {
        Thread {
            server = createServer()
            server?.start(wait = false)
        }.start()
    }

    private fun createServer() = embeddedServer(Netty, port = SERVER_PORT) {
        // Configuración del motor de plantillas para el HTML
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "assets")
        }

        // Configuración de las rutas del servidor en el navegador
        routing { controller.setupRoutes(this) }
    }

    override fun onDestroy() {
        controller.clearDataFile()
        server?.stop(1000, 5000)

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}