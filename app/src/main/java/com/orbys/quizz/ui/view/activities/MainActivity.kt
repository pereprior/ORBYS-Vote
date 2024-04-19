package com.orbys.quizz.ui.view.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.databinding.ActivityMainBinding
import com.orbys.quizz.ui.view.fragments.TypesQuestionFragment
import com.orbys.quizz.ui.services.FloatingViewService
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stopService(Intent(this, HttpService::class.java))
        stopService(Intent(this, FloatingViewService::class.java))

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        printFragments()
    }

    private fun printFragments() {

        with(binding) {

            closeButton.setOnClickListener {
                finish()
                exitProcess(0)
            }

            supportFragmentManager.beginTransaction().apply {
                // Fragmento con los tipos de preguntas
                replace(fragmentContainer.id, TypesQuestionFragment())
                commit()
            }
        }

    }

}