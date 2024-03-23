package com.pprior.quizz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.databinding.ActivityMainBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.ListFragment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import com.pprior.quizz.data.server.module
import io.ktor.server.application.Application
import io.ktor.server.netty.NettyApplicationEngine

/**
 * MainActivity es la actividad principal de la aplicación.
 *
 * Se encarga de la creación de los fragmentos y del servidor HTTP.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var server: NettyApplicationEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Agregamos los fragmentos al contenedor
        supportFragmentManager.beginTransaction().apply {
            // Barra superior
            replace(binding.headerFragment.id, HeadFragment())
            // Lista de preguntas
            replace(binding.fragmentListRecyclerView.id, ListFragment())
            commit()
        }

        // inicamos el servidor http
        startServer()
    }

    private fun startServer() {
        server = embeddedServer(Netty, port = 8888, module = Application::module)
        server.start(wait = false)
    }

    override fun onDestroy() {
        server.stop(0, 0)
        super.onDestroy()
    }
}