package com.orbys.quizz.ui.view.fragments.add

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.orbys.quizz.R
import com.orbys.quizz.domain.models.Answer
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.domain.models.Question
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Clase que representa una actividad para añadir preguntas de tipo "Si/No".
 */
class AddYesNoQuestion: AddFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val title: TextView? = activity?.findViewById(R.id.title)
            title?.text = "${getString(R.string.yesno_question_type_title)} ${getString(R.string.question_question_hint)}"
            questionTypeIcon.setImageResource(R.drawable.ic_yesno)

            filterUsersTitle.visibility = View.GONE
            filterUsersGroup.visibility = View.GONE
            filterUsersDivider.visibility = View.GONE
            multiAnswerTitle.visibility = View.GONE
            multiAnswerGroup.visibility = View.GONE
            multiAnswerDivider.visibility = View.GONE
        }

    }

    override fun createQuestionFromInput() = Question(
        question = binding.questionQuestion.text.toString(),
        icon = R.drawable.ic_yesno,
        answers = MutableStateFlow(
            listOf(
                Answer(this.getString(R.string.yes_answer_placeholder)),
                Answer(this.getString(R.string.no_answers_placeholder))
            )
        ),
        answerType = AnswerType.BOOLEAN,
        isAnonymous = binding.anonymousQuestionOption.isChecked,
        timeOut = binding.timeoutInput.text.toString().toIntOrNull() ?: 0
    )

}