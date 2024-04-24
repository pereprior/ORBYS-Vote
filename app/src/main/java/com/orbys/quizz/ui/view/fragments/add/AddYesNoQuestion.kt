package com.orbys.quizz.ui.view.fragments.add

import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import com.orbys.quizz.ui.components.managers.AnswerRadioButtonManager
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Si/No".
 */
class AddYesNoQuestion: AddFragment() {

    private lateinit var answerManager: AnswerRadioButtonManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            questionTypeIcon.setImageResource(R.drawable.ic_yesno)
            answerManager = AnswerRadioButtonManager(
                context = requireContext(),
                layout = binding.answersLayout
            )

            filterUsersTitle.visibility = View.GONE
            filterUsersGroup.visibility = View.GONE
            filterUsersDivider.visibility = View.GONE
            multiAnswerTitle.visibility = View.GONE
            multiAnswerGroup.visibility = View.GONE
            multiAnswerDivider.visibility = View.GONE
        }

        answerManager.createRadioGroup()
    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.ic_yesno,
        answers = MutableStateFlow(
            when (answerManager.getCheckedRadioButtonId()) {
                R.id.non_filter_users_question_option -> listOf(
                    Answer(this.getString(R.string.yes_answer_placeholder)),
                    Answer(this.getString(R.string.no_answers_placeholder))
                )
                else -> listOf(
                    Answer(this.getString(R.string.true_answer_placeholder)),
                    Answer(this.getString(R.string.false_answer_placeholder))
                )
            }
        ),
        answerType = AnswerType.YESNO,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}