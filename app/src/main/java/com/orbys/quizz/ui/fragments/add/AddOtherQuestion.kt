package com.orbys.quizz.ui.fragments.add

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Otros".
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
class AddOtherQuestion: AddFragment() {

    private val answerFields = mutableListOf<EditText>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefaultAnswers()
    }

    private fun setDefaultAnswers() {
        addNewAnswerButton()

        addNewAnswerToQuestion()
        addNewAnswerToQuestion()
    }

    private fun addNewAnswerToQuestion() {
        // Crear un nuevo campo de texto para una respuesta
        val newAnswerField = EditText(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            hint = getString(R.string.question_answer)
            inputType = InputType.TYPE_CLASS_TEXT
            id = View.generateViewId()
            filters = arrayOf(InputFilter.LengthFilter(50))
        }

        // Agregar el nuevo campo de texto al layout y a la lista de campos de texto
        binding.answersLayout.addView(newAnswerField)
        answerFields.add(newAnswerField)
    }

    private fun addNewAnswerButton() {
        // Crear el boton para añadir nuevas respuestas
        val newButton = ImageButton(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            foregroundGravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, android.R.color.transparent)
            contentDescription = getString(R.string.exit_button)

            val drawable = ContextCompat.getDrawable(context, android.R.drawable.ic_input_add)
            val originalIconSize = maxOf(drawable?.intrinsicWidth ?: 0, drawable?.intrinsicHeight ?: 0)
            val desiredIconSize = context.resources.getDimensionPixelSize(R.dimen.icon_size)
            val scale = desiredIconSize.toFloat() / originalIconSize

            setImageDrawable(drawable)
            scaleX = scale
            scaleY = scale

            setColorFilter(ContextCompat.getColor(context, R.color.blue_selected), PorterDuff.Mode.SRC_IN)
            setOnClickListener {
                if (answerFields.size < 5) {
                    addNewAnswerToQuestion()
                } else {
                    binding.errorMessage.text = getString(R.string.error_max_answers)
                }
            }
        }

        binding.answersLayout.addView(newButton)
    }

    override fun saveQuestion(context: Context) {
        if (answerFields.any { it.text.isEmpty() }) {
            binding.errorMessage.text = getString(R.string.error_empty_answers)
            return
        }

        super.saveQuestion(context)
    }

    override fun createQuestionFromInput(): Question {
        val questionText = binding.questionQuestion.text.toString()
        val answers = answerFields.map { Answer(it.text.toString()) }

        return Question(
            question = questionText,
            icon = R.drawable.baseline_menu_24,
            answers = answers,
            answerType = AnswerType.OTHER,
            multipleChoice = true
        )
    }

}