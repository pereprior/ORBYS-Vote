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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var server: NettyApplicationEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar el fragmento de la barra superior
        val headFragment = HeadFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.headerFragment.id, headFragment)
            .commit()

        // Cargar el fragmento de la lista de preguntas
        val listFragment = ListFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragmentListRecyclerView.id, listFragment)
            .commit()

        // Iniciar el servidor Ktor
        server = embeddedServer(Netty, port = 8888, module = Application::module)
        server.start(wait = false)
    }

    override fun onDestroy() {
        server.stop(0, 0)
        super.onDestroy()
    }
}