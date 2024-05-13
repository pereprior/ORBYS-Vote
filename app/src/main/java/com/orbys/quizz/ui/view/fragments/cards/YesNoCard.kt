package com.orbys.quizz.ui.view.fragments.cards

import com.orbys.quizz.R
import com.orbys.quizz.databinding.CardTypesQuestionBinding
import com.orbys.quizz.ui.view.fragments.add.AddYesNoQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo si/no.
 */
class YesNoCard: QuestionTypesCard() {
    override fun CardTypesQuestionBinding.bindCard() {
        cardTitle.text = context?.getText(R.string.yesno_question_type_title)
        cardIcon.setImageResource(R.drawable.ic_yesno)
    }

    override fun setOnClickCard() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, AddYesNoQuestion())
            addToBackStack(null)
            commit()
        }
    }

}