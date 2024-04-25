package com.orbys.quizz.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.ui.view.fragments.cards.BooleanCard
import com.orbys.quizz.ui.view.fragments.cards.NumericCard
import com.orbys.quizz.ui.view.fragments.cards.OtherCard
import com.orbys.quizz.ui.view.fragments.cards.StarsCard
import com.orbys.quizz.ui.view.fragments.cards.YesNoCard

/**
 * Fragmento que contiene los tipos de preguntas que se pueden crear.
 */
class TypesQuestionFragment: Fragment() {

    private lateinit var binding: FragmentQuestionTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionTypesBinding.inflate(inflater, container, false)

        with(binding) {
            val button: ImageButton = activity?.findViewById(R.id.back_button) ?: return root
            button.visibility = View.GONE

            parentFragmentManager.beginTransaction()
                .add(yesNoCardContainer.id, YesNoCard())
                .add(booleanCardContainer.id, BooleanCard())
                .add(starsCardContainer.id, StarsCard())
                .add(numericCardContainer.id, NumericCard())
                .add(otherCardContainer.id, OtherCard())
                .commit()

            return root
        }
    }

}