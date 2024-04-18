package com.orbys.quizz.ui.view.fragments.cards

import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.ui.view.fragments.add.AddYesNoQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo si/no.
 */
class YesNoCard: QuestionTypesCard() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, AddYesNoQuestion())
                addToBackStack(null)
                commit()
            }
        }

        with(binding) {
            cardTitle.text = context?.getText(R.string.yesno_question_type_title)
            cardIcon.setImageResource(R.drawable.ic_yesno)
        }
    }

}