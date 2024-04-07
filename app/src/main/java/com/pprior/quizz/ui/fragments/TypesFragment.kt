package com.pprior.quizz.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pprior.quizz.databinding.FragmentTypesBinding
import com.pprior.quizz.ui.activities.dialogs.types.AddBarQuestion
import com.pprior.quizz.ui.activities.dialogs.types.AddOtherQuestion
import com.pprior.quizz.ui.activities.dialogs.types.AddStarsQuestion
import com.pprior.quizz.ui.activities.dialogs.types.AddYesNoQuestion

/**
 * Fragmento que muestra los diferentes tipos de preguntas que se pueden crear.
 * Cada tipo de pregunta se representa con una tarjeta que, al hacer clic en ella, inicia la actividad correspondiente para crear una pregunta de ese tipo.
 *
 * @property binding Enlace con los elementos de la vista del fragmento.
 */
class TypesFragment : Fragment() {

    private lateinit var binding: FragmentTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTypesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setQuestionTypesCards()
    }

    private fun setQuestionTypesCards() {
        // Mapa con las tarjetas de los diferentes tipos de preguntas.
        val questionTypesCards = mapOf(
            binding.yesNoCard to AddYesNoQuestion::class.java,
            binding.starCard to AddStarsQuestion::class.java,
            binding.barCard to AddBarQuestion::class.java,
            binding.otherCard to AddOtherQuestion::class.java
        )

        // Al clickar, se inicia la actividad correspondiente para crear una pregunta de ese tipo.
        questionTypesCards.forEach { (view, addQuestionActivity) ->
            view.setOnClickListener {
                startActivity(Intent(context, addQuestionActivity))
                activity?.finish()
            }
        }
    }

}