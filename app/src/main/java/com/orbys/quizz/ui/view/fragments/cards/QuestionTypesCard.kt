package com.orbys.quizz.ui.view.fragments.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbys.quizz.databinding.CardTypesQuestionBinding

/**
 * Clase que sirve como base para los fragmentos que representan a las tarjetas de tipos de preguntas.
 *
 * @property binding Enlace común a la vista que comparten todas las clases que extienden.
 */
abstract class QuestionTypesCard : Fragment() {

    private lateinit var binding: CardTypesQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CardTypesQuestionBinding.inflate(inflater, container, false)
        binding.bindCard()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener { setOnClickCard() }
    }

    protected abstract fun CardTypesQuestionBinding.bindCard()
    protected abstract fun setOnClickCard()

}