package com.pprior.quizz.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.databinding.ActivityQuestionTypeBinding
import com.pprior.quizz.ui.fragments.HeadFragment
import com.pprior.quizz.ui.fragments.TypesFragment

class QuestionTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        printFragments()
    }

    private fun printFragments() {
        supportFragmentManager.beginTransaction().apply {
            // Barra superior
            replace(binding.headerFragment.id, HeadFragment())
            // Lista de preguntas
            replace(binding.cardsTypes.id, TypesFragment())
            commit()
        }
    }

}