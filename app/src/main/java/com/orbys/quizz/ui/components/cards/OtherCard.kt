package com.orbys.quizz.ui.components.cards

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.ui.fragments.add.AddOtherQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo otros.
 */
class OtherCard : QuestionTypesCard() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            startActivity(Intent(context, AddOtherQuestion::class.java))
            activity?.finish()
        }

        with(binding) {
            cardTitle.text = context?.getText(R.string.other_title)
            cardIcon.setImageResource(R.drawable.baseline_menu_24)
            cardDescription.text = context?.getText(R.string.other_desc)
        }
    }

}