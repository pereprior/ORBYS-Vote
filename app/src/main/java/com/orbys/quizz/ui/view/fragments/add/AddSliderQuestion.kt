package com.orbys.quizz.ui.view.fragments.add

import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Barra progresiva".
 */
class AddSliderQuestion: AddFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            binding.questionTypeIcon.setImageResource(R.drawable.ic_slider)
            multiAnswerGroup.visibility = View.GONE
            multiAnswerDivider.visibility = View.GONE
        }
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.ic_slider,
        answers = listOf(
            Answer(1),
            Answer(2),
            Answer(3),
            Answer(4),
            Answer(5),
        ),
        answerType = AnswerType.BAR,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0,
        isMultipleAnswers = binding.nonFilterUsersQuestionOption.isChecked
    )

}