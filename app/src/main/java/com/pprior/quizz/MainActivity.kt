package com.pprior.quizz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.data.server.HttpService
import com.pprior.quizz.databinding.ActivityMainBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.ListFragment
import com.pprior.quizz.data.server.di.QuestionComponent
import com.pprior.quizz.data.server.repositories.QuestionRepositoryImp
import com.pprior.quizz.domain.viewModels.QuestionViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

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

        // Inyeccion de dependencias
        startKoin {
            modules(
                module {
                    single { QuestionViewModel() }
                    single { QuestionComponent() }
                    single { QuestionRepositoryImp() }
                    single {  }
                }
            )
        }

        // inicamos el servidor http
        startService(Intent(this, HttpService::class.java))

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