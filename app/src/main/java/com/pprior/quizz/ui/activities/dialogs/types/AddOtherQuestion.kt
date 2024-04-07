package com.pprior.quizz.ui.activities.dialogs.types

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pprior.quizz.R
import com.pprior.quizz.databinding.ActivityAddOtherQuestionBinding
import com.pprior.quizz.domain.models.Answer
import com.pprior.quizz.domain.models.AnswerType
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.viewModels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Otros".
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
class AddOtherQuestion: AppCompatActivity() {

    private val viewModel by viewModels<QuestionViewModel>()
    private lateinit var binding: ActivityAddOtherQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOtherQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        if (viewModel.existsQuestion(question)) {
            binding.errorMessage.text = getString(R.string.questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos la actividad
            viewModel.addQuestion(question)
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
        ),
        answerType = AnswerType.OTHER
    )

}