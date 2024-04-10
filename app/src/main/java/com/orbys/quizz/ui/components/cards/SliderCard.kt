package com.orbys.quizz.ui.components.cards

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.orbys.quizz.R
import com.orbys.quizz.ui.fragments.add.AddSliderQuestion

/**
 * Clase que representa la tarjeta para crear preguntas de tipo barra progresiva.
 */
class SliderCard : QuestionTypesCard() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            startActivity(Intent(context, AddSliderQuestion::class.java))
            activity?.finish()
        }

        with(binding) {
            cardTitle.text = context?.getText(R.string.bar_title)
            cardIcon.setImageResource(R.drawable.baseline_space_bar_24)
            cardDescription.text = context?.getText(R.string.bar_desc)
        }
    }

}