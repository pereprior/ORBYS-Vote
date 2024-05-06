package com.orbys.quizz.ui.view.fragments.cards

import com.orbys.quizz.R
import com.orbys.quizz.databinding.CardTypesQuestionBinding
import com.orbys.quizz.ui.view.fragments.add.AddNumericQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo barra progresiva.
 */
class NumericCard : QuestionTypesCard() {
    override fun CardTypesQuestionBinding.bindCard() {
        cardTitle.text = context?.getText(R.string.numeric_question_type_title)
        cardIcon.setImageResource(R.drawable.ic_number)
    }

    override fun setOnClickCard() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, AddNumericQuestion())
            addToBackStack(null)
            commit()
        }
    }
}