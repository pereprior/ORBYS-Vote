package com.orbys.quizz.ui.view.fragments.cards

import com.orbys.quizz.R
import com.orbys.quizz.databinding.CardTypesQuestionBinding
import com.orbys.quizz.ui.view.fragments.add.AddOtherQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo otros.
 */
class OtherCard : QuestionTypesCard() {
    override fun CardTypesQuestionBinding.bindCard() {
        cardTitle.text = context?.getText(R.string.other_question_type_title)
        cardIcon.setImageResource(R.drawable.ic_others)
    }

    override fun setOnClickCard() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, AddOtherQuestion())
            addToBackStack(null)
            commit()
        }
    }
}