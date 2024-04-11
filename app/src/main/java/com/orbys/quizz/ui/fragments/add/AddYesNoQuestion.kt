package com.orbys.quizz.ui.fragments.add

import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Si/No".
 */
class AddYesNoQuestion: AddFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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