package com.pprior.quizz.ui.activities.dialogs.types

import android.os.Bundle
import com.pprior.quizz.R
import com.pprior.quizz.domain.models.Answer
import com.pprior.quizz.domain.models.AnswerType
import com.pprior.quizz.domain.models.Question
import com.pprior.quizz.ui.activities.dialogs.AddQuestionActivity

class AddBarQuestion: AddQuestionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.questionTypeIcon.setImageResource(R.drawable.baseline_space_bar_24)
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.baseline_space_bar_24,
        answers = listOf(
            Answer(1f),
            Answer(2f),
            Answer(3f),
            Answer(4f),
            Answer(5f),
        ),
        answerType = AnswerType.BAR
    )

}