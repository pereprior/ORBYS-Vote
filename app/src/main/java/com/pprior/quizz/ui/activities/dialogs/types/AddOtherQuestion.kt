package com.pprior.quizz.ui.activities.dialogs.types

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.R
import com.pprior.quizz.data.flow.FlowRepository
import com.pprior.quizz.databinding.ActivityAddOtherQuestionBinding
import com.pprior.quizz.domain.models.Answer
import com.pprior.quizz.domain.models.Question
import org.koin.java.KoinJavaComponent

class AddOtherQuestion: AppCompatActivity() {

    private val repository: FlowRepository by KoinJavaComponent.inject(FlowRepository::class.java)
    private lateinit var binding: ActivityAddOtherQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOtherQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("AddOtherQuestion", "AAAAAAAA")

        with(binding) {
            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion() }
            closeButton.setOnClickListener { finish() }
        }
    }

    private fun saveQuestion() {
        val question = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (repository.exists(question)) {
            binding.errorMessage.text = getString(R.string.questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos la actividad
            repository.addQuestion(question)
            finish()
        }
    }

    private fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.baseline_menu_24,
        answers = listOf(
            Answer(binding.questionAnswer1.text),
            Answer(binding.questionAnswer2.text),
            Answer(binding.questionAnswer3.text)
        )
    )

}