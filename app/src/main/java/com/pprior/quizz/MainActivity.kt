package com.pprior.quizz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.data.server.HttpService
import com.pprior.quizz.databinding.ActivityMainBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.ListFragment

/**
 * MainActivity es la actividad principal de la aplicación.
 *
 * Se encarga de la creación de los fragmentos y del servidor HTTP.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        startService(Intent(this, HttpService::class.java))
    }

}