package com.orbys.quizz.ui.view.fragments.add

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

        binding.questionTypeIcon.setImageResource(R.drawable.ic_others)
        setDefaultAnswers()
    }

    override fun saveQuestion(context: Context) {
        // Controlar que los campos de las preguntas no estén vacíos
        if (answerFields.any { it.text.isEmpty() }) {
            binding.errorMessage.text = getString(R.string.empty_answers_error)
            return
        }

        // Controlar que no haya dos preguntas iguales
        val answerTexts = answerFields.map { it.text.toString() }
        if (answerTexts.size != answerTexts.toSet().size) {
            binding.errorMessage.text = getString(R.string.same_question_error)
            return
        }

        super.saveQuestion(context)
    }

    override fun createQuestionFromInput(): Question {
        val questionText = binding.questionQuestion.text.toString()
        val answers = answerFields.map { Answer(it.text.toString()) }

        return Question(
            question = questionText,
            icon = R.drawable.ic_others,
            answers = answers,
            answerType = AnswerType.OTHER,
            isAnonymous = binding.anonymousQuestionOption.isChecked,
            timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
            isMultipleChoices = binding.multiAnswerQuestionOption.isChecked,
            isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
        )
    }

    private fun setDefaultAnswers() {
        // Boton para añadir mas respuestas
        addNewAnswerButton()

        // Añadir dos respuestas por defecto
        repeat(2) { addNewAnswerToQuestion() }
    }

    private fun addNewAnswerToQuestion() {
        val newAnswerField = createAnswerField()
        // Agregar el nuevo campo de texto al layout y a la lista de campos de texto
        binding.answersLayout.addView(newAnswerField)
        answerFields.add(newAnswerField)
    }

    private fun createAnswerField() = EditText(context).apply {
        // Crear un nuevo campo de texto para una respuesta
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hint = getString(R.string.question_answer_hint)
        inputType = InputType.TYPE_CLASS_TEXT
        id = View.generateViewId()
        filters = arrayOf(InputFilter.LengthFilter(20))
    }

    private fun addNewAnswerButton() {
        val newButton = createNewAnswerButton()
        binding.answersLayout.addView(newButton)
    }

    private fun createNewAnswerButton() = ImageButton(context).apply {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        setupButtonAppearance()
        setOnClickListener {
            if (answerFields.size < 5) addNewAnswerToQuestion()
            else binding.errorMessage.text = getString(R.string.max_answers_error)
        }
    }

    private fun ImageButton.setupButtonAppearance() {
        val drawable = ContextCompat.getDrawable(context, android.R.drawable.ic_input_add)
        val originalIconSize = maxOf(drawable?.intrinsicWidth ?: 0, drawable?.intrinsicHeight ?: 0)
        val desiredIconSize = context.resources.getDimensionPixelSize(R.dimen.icon_size)
        val scale = desiredIconSize.toFloat() / originalIconSize

        foregroundGravity = Gravity.CENTER
        background = ContextCompat.getDrawable(context, android.R.color.transparent)
        contentDescription = getString(R.string.exit_button_desc)
        scaleX = scale
        scaleY = scale

        setImageDrawable(drawable)
        setColorFilter(ContextCompat.getColor(context, R.color.blue_selected), PorterDuff.Mode.SRC_IN)
    }

}