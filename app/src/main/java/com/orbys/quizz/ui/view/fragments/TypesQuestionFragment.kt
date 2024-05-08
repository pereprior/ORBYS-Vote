package com.orbys.quizz.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.replaceMainActivityBindingFunctions
import com.orbys.quizz.core.extensions.stopActiveServices
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.ui.components.HotspotDialog
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

        // Detiene los servicios activos
        stopActiveServices()

        // Cambios en los elementos de la actividad principal
        replaceMainActivityBindingFunctions(
            titleRedId = R.string.activity_question_type_title,
            closeButtonVisibility = View.VISIBLE
        )

        with(binding) {

            // Configura las tarjetas de los tipos de preguntas
            parentFragmentManager.beginTransaction()
                .add(yesNoCardContainer.id, YesNoCard())
                .add(booleanCardContainer.id, BooleanCard())
                .add(starsCardContainer.id, StarsCard())
                .add(numericCardContainer.id, NumericCard())
                .add(otherCardContainer.id, OtherCard())
                .commit()

            hotspotQr.setOnClickListener {
                HotspotDialog(requireContext()).show()
            }

            return root
        }

    }

}