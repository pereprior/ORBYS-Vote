package com.orbys.quizz.ui.components.cards

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.ui.fragments.add.AddStarsQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo estrellas.
 */
class StarsCard : QuestionTypesCard() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            startActivity(Intent(context, AddStarsQuestion::class.java))
            activity?.finish()
        }

        with(binding) {
            cardTitle.text = context?.getText(R.string.stars_title)
            cardIcon.setImageResource(R.drawable.baseline_star_rate_24)
            cardDescription.text = context?.getText(R.string.stars_desc)
        }
    }

}