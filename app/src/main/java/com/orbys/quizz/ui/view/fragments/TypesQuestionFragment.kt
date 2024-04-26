package com.orbys.quizz.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.data.services.HttpService
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.ui.services.FloatingViewService
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
        stopActiveServices()

        with(binding) {
            val backButton: ImageButton = activity?.findViewById(R.id.back_button) ?: return root
            val closeButton: ImageButton = activity?.findViewById(R.id.close_button) ?: return root
            val title: TextView = activity?.findViewById(R.id.title) ?: return root

            closeButton.visibility = View.VISIBLE
            backButton.visibility = View.GONE
            title.text = getString(R.string.activity_question_type_title)

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

    private fun stopActiveServices() {
        activity?.stopService(Intent(activity, HttpService::class.java))
        activity?.stopService(Intent(activity, FloatingViewService::class.java))
    }

}