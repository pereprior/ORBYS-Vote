package com.orbys.quizz.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.ui.components.cards.OtherCard
import com.orbys.quizz.ui.components.cards.SliderCard
import com.orbys.quizz.ui.components.cards.StarsCard
import com.orbys.quizz.ui.components.cards.YesNoCard

class TypesQuestionFragment: Fragment() {

    private lateinit var binding: FragmentQuestionTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionTypesBinding.inflate(inflater, container, false)

        addFragments()

        return binding.root
    }

    private fun addFragments() {
        parentFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, YesNoCard())
            .add(binding.fragmentContainer.id, StarsCard())
            .add(binding.fragmentContainer.id, SliderCard())
            .add(binding.fragmentContainer.id, OtherCard())
            .commit()
    }

}