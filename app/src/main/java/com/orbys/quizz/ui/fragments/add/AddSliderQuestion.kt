package com.orbys.quizz.ui.fragments.add

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

        binding.questionTypeIcon.setImageResource(R.drawable.baseline_space_bar_24)
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.baseline_space_bar_24,
        answers = listOf(
            Answer(1),
            Answer(2),
            Answer(3),
            Answer(4),
            Answer(5),
        ),
        answerType = AnswerType.BAR,
        anonymous = binding.anonymousQuestionOption.isChecked
    )

}