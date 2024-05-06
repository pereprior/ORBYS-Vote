package com.orbys.quizz.ui.view.fragments.cards

import com.orbys.quizz.R
import com.orbys.quizz.databinding.CardTypesQuestionBinding
import com.orbys.quizz.ui.view.fragments.add.AddBooleanQuestion

class BooleanCard: QuestionTypesCard() {
    override fun CardTypesQuestionBinding.bindCard() {
        cardTitle.text = context?.getText(R.string.truefalse_question_type_title)
        cardIcon.setImageResource(R.drawable.ic_boolean)
    }

    override fun setOnClickCard() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, AddBooleanQuestion())
            addToBackStack(null)
            commit()
        }
    }
}