package com.pprior.quizz.ui.fragments.types

import android.os.Bundle
import android.view.View
import com.pprior.quizz.R

/**
 * Clase que representa la tarjeta para crear preguntas de tipo otros.
 */
class OtherItemFragment : TypesItemFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            cardTitle.text = context?.getText(R.string.other_title)
            cardIcon.setImageResource(R.drawable.baseline_menu_24)
            cardDescription.text = context?.getText(R.string.other_desc)
        }
    }

}