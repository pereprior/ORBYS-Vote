package com.pprior.quizz.data.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pprior.quizz.data.server.controllers.questionController
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.install
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing

class HttpService: Service() {
    private var server: ApplicationEngine? = null

    override fun onCreate() {
        super.onCreate()

        Thread {
            // Inicializacion del servidor http
            server = embeddedServer(Netty, port = 8888){
                install(FreeMarker) {
                    templateLoader = ClassTemplateLoader(this::class.java.classLoader, "assets")
                }

                routing {
                    questionController()
                }

            }
            server?.start(wait = false)

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        server?.stop(1000, 5000)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}