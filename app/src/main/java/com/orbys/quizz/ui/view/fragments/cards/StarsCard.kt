package com.orbys.quizz.ui.view.fragments.cards

import com.orbys.quizz.R
import com.orbys.quizz.databinding.CardTypesQuestionBinding
import com.orbys.quizz.ui.view.fragments.add.AddStarsQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo estrellas.
 */
class StarsCard : QuestionTypesCard() {
    override fun CardTypesQuestionBinding.bindCard() {
        cardTitle.text = context?.getText(R.string.stars_question_type_title)
        cardIcon.setImageResource(R.drawable.ic_star)
    }

    override fun setOnClickCard() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, AddStarsQuestion())
            addToBackStack(null)
            commit()
        }
    }
}