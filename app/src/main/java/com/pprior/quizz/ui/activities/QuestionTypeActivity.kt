package com.pprior.quizz.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.databinding.ActivityQuestionTypeBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.TypesFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * QuestionTypeActivity es la actividad donde podemos seleccionar el tipo de pregunta que queremos crear.
 */
@AndroidEntryPoint
class QuestionTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Agregamos los fragmentos al contenedor
        printFragments()
    }

    private fun printFragments() {
        supportFragmentManager.beginTransaction().apply {
            // Barra superior
            replace(binding.headerFragment.id, HeadFragment::class.java, null)
            // Seleccion con los diferentes tipos de preguntas
            replace(binding.cardsTypes.id, TypesFragment::class.java, null)
            commit()
        }
    }

}