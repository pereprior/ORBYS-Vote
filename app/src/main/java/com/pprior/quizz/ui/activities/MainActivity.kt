package com.pprior.quizz.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.databinding.ActivityMainBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.ListFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity es la actividad principal de la aplicación.
 *
 * Contiene la lista de las preguntas.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Agregamos los fragmentos al contenedor
        printFragments()
    }

    private fun printFragments() {
        supportFragmentManager.beginTransaction().apply {
            // Barra superior
            replace(binding.headerFragment.id, HeadFragment())
            // Lista de preguntas
            replace(binding.fragmentListRecyclerView.id, ListFragment())
            commit()
        }
    }

}