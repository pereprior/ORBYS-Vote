package com.pprior.quizz.ui.fragments.types

import android.os.Bundle
import android.view.View
import com.pprior.quizz.R

/**
 * Clase que representa la tarjeta para crear preguntas de tipo estrellas.
 */
class StarsItemFragment : TypesItemFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            cardTitle.text = context?.getText(R.string.stars_title)
            cardIcon.setImageResource(R.drawable.baseline_star_rate_24)
            cardDescription.text = context?.getText(R.string.stars_desc)
        }
    }

}