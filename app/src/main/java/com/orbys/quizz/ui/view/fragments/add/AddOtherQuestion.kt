package com.orbys.quizz.ui.view.fragments.add

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.components.managers.AnswerFieldsManager
import dagger.hilt.android.AndroidEntryPoint

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Otros".
 *
 * @property viewModel ViewModel para gestionar las operaciones relacionadas con las preguntas.
 * @property binding Objeto de enlace para acceder a los elementos de la interfaz de usuario.
 */
@AndroidEntryPoint
class AddOtherQuestion: AddFragment() {

    private lateinit var fieldsManager: AnswerFieldsManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldsManager = AnswerFieldsManager(requireContext(), binding.answersLayout)
        binding.questionTypeIcon.setImageResource(R.drawable.ic_others)
        setDefaultAnswers()
    }

    override fun saveQuestion(context: Context) {
        // Controlar que los campos de las preguntas no estén vacíos
        if (fieldsManager.anyAnswerIsEmpty()) {
            errorMessage(R.string.empty_answers_error)
            return
        }

        // Controlar que no haya dos preguntas iguales
        val answerTexts = fieldsManager.getAnswersText()
        if (answerTexts.size != answerTexts.toSet().size) {
            errorMessage(R.string.same_question_error)
            return
        }

        super.saveQuestion(context)
    }

    override fun createQuestionFromInput(): Question {
        val questionText = binding.questionQuestion.text.toString()

        return Question(
            question = questionText,
            icon = R.drawable.ic_others,
            answers = fieldsManager.getAnswers(),
            answerType = AnswerType.OTHER,
            isAnonymous = binding.anonymousQuestionOption.isChecked,
            timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
            isMultipleChoices = binding.multiAnswerQuestionOption.isChecked,
            isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
        )
    }

    private fun setDefaultAnswers() {
        // Boton para añadir mas respuestas
        addEditAnswersButtons()

        // Añadir dos respuestas por defecto
        repeat(2) { fieldsManager.addAnswerField() }
    }

    private fun addEditAnswersButtons() {
        val addButton = createButton(android.R.drawable.ic_input_add)
        val deleteButton = createButton(android.R.drawable.ic_input_delete)

        addButton.setOnClickListener {
            if (fieldsManager.answersNumber() < 5) fieldsManager.addAnswerField()
            else errorMessage(R.string.max_answers_error)
        }

        deleteButton.setOnClickListener {
            if (fieldsManager.answersNumber() > 2) fieldsManager.removeAnswerField()
            else errorMessage(R.string.min_answers_error)
        }

        // layout para contener los botones
        val buttonLayout = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(addButton)
            addView(deleteButton)
        }

        binding.answersLayout.addView(buttonLayout)
    }

    private fun createButton(resId: Int) = ImageButton(context).apply {
        val drawable = ContextCompat.getDrawable(context, resId)
        val originalIconSize = maxOf(drawable?.intrinsicWidth ?: 0, drawable?.intrinsicHeight ?: 0)
        val desiredIconSize = context.resources.getDimensionPixelSize(R.dimen.icon_size)
        val scale = desiredIconSize.toFloat() / originalIconSize

        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        foregroundGravity = Gravity.CENTER
        background = ContextCompat.getDrawable(context, android.R.color.transparent)
        scaleX = scale
        scaleY = scale
        setColorFilter(ContextCompat.getColor(context, R.color.blue_selected), PorterDuff.Mode.SRC_IN)
        setImageDrawable(drawable)
    }

}