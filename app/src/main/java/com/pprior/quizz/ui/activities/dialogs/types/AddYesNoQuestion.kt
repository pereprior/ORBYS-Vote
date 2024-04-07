package com.pprior.quizz.ui.activities.dialogs.types

import android.os.Bundle
import com.pprior.quizz.R
import com.pprior.quizz.domain.models.Answer
import com.pprior.quizz.domain.models.AnswerType
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.activities.dialogs.AddQuestionActivity

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Si/No".
 */
class AddYesNoQuestion: AddQuestionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.questionTypeIcon.setImageResource(R.drawable.baseline_done_all_24)
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.baseline_done_all_24,
        answers = listOf(
            Answer(this.getString(R.string.yes_aswers)),
            Answer(this.getString(R.string.no_aswers))
        ),
        answerType = AnswerType.YESNO
    )

}