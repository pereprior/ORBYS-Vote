package com.orbys.quizz.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.ui.view.fragments.cards.OtherCard
import com.orbys.quizz.ui.view.fragments.cards.SliderCard
import com.orbys.quizz.ui.view.fragments.cards.StarsCard
import com.orbys.quizz.ui.view.fragments.cards.YesNoCard
import kotlin.system.exitProcess

class TypesQuestionFragment: Fragment() {

    private lateinit var binding: FragmentQuestionTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionTypesBinding.inflate(inflater, container, false)

        val button: ImageButton = activity?.findViewById(R.id.back_button) ?: return binding.root
        button.visibility = View.GONE

        addFragments()

        return binding.root
    }

    private fun addFragments() {
        parentFragmentManager.beginTransaction()
            .add(binding.yesNoCardContainer.id, YesNoCard())
            .add(binding.starsCardContainer.id, StarsCard())
            .add(binding.sliderCardContainer.id, SliderCard())
            .add(binding.otherCardContainer.id, OtherCard())
            .commit()
    }

}