package com.pprior.quizz.ui.activities.dialogs.types

import android.os.Bundle
import com.pprior.quizz.R
import com.pprior.quizz.domain.models.Answer
import com.pprior.quizz.domain.models.AnswerType
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.activities.dialogs.AddQuestionActivity

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Estrellas".
 */
class AddStarsQuestion: AddQuestionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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