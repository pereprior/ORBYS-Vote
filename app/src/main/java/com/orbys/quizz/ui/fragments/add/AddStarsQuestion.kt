package com.orbys.quizz.ui.fragments.add

import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Estrellas".
 */
class AddStarsQuestion: AddFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.questionTypeIcon.setImageResource(R.drawable.baseline_star_rate_24)

    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.baseline_star_rate_24,
        answers = listOf(
            Answer(1),
            Answer(2),
            Answer(3),
            Answer(4),
            Answer(5),
        ),
        answerType = AnswerType.STARS
    )

}