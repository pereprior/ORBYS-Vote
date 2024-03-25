package com.pprior.quizz.data.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pprior.quizz.data.server.controllers.questionController
import com.pprior.quizz.data.server.di.QuestionComponent
import com.pprior.quizz.data.server.repositories.QuestionRepositoryImp
import com.pprior.quizz.ui.viewmodels.QuestionViewModel
import freemarker.cache.ClassTemplateLoader
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.freemarker.FreeMarker
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import org.koin.core.context.startKoin
import org.koin.dsl.module

class HttpService: Service() {

    override fun onCreate() {
        super.onCreate()
        val viewModel = QuestionViewModel()

        Thread {

            // Inyeccion de dependencias
            startKoin {
                modules(
                    module {
                        single { QuestionComponent() }
                        single { QuestionRepositoryImp(viewModel) }
                    }
                )
            }

            // Inicializacion del servidor http
            embeddedServer(Netty, port = 8888){

                install(FreeMarker) {
                    templateLoader = ClassTemplateLoader(this::class.java.classLoader, "static/templates")
                }

                routing {
                    questionController()
                }

            }.start(wait = true)

        }.start()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}