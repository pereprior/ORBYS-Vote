package com.orbys.quizz.ui.fragments.add

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.orbys.quizz.R
import com.orbys.quizz.databinding.ActivityAddOtherQuestionBinding
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.MainActivity
import com.orbys.quizz.ui.viewModels.QuestionViewModel
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
    private val answerFields = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOtherQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDefaultAnswers()

        with(binding) {
            // Asignar los listeners a los botones
            saveButton.setOnClickListener { saveQuestion() }
            closeButton.setOnClickListener {
                startActivity(Intent(it.context, MainActivity::class.java))
                finish()
            }
            addAnswerButton.setOnClickListener {
                if (answerFields.size < 5) {
                    addNewAnswerToQuestion()
                } else {
                    errorMessage.text = getString(R.string.error_max_answers)
                }
            }
        }

    }

    private fun setDefaultAnswers() {
        addNewAnswerToQuestion()
        addNewAnswerToQuestion()
    }

    private fun addNewAnswerToQuestion() {
        // Crear un nuevo campo de texto para una respuesta
        val newAnswerField = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            hint = getString(R.string.question_answer)
            inputType = InputType.TYPE_CLASS_TEXT
            id = View.generateViewId()
        }

        // Agregar el nuevo campo de texto al layout y a la lista de campos de texto
        binding.answersLayout.addView(newAnswerField)
        answerFields.add(newAnswerField)
    }

    private fun saveQuestion() {
        val question = createQuestionFromInput()

        // Comprobar si la pregunta o el título están vacíos
        if (question.question.isEmpty()) {
            return
        }

        // Si ya existe la pregunta, mostrar un mensaje de error
        if (viewModel.existsQuestion(question)) {
            binding.errorMessage.text = getString(R.string.error_questions_exists)
        } else {
            // Guardamos la pregunta en la lista y cerramos la actividad
            viewModel.addQuestion(question)
            finish()
        }
    }

    private fun createQuestionFromInput(): Question {
        val questionText = binding.questionQuestion.text.toString()
        val answers = answerFields.map { Answer(it.text.toString()) }

        return Question(
            question = questionText,
            icon = R.drawable.baseline_menu_24,
            answers = answers,
            answerType = AnswerType.OTHER
        )
    }

}