package com.orbys.vote.ui.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.core.extensions.stopActiveServices
import com.orbys.vote.databinding.FragmentQuestionTypesBinding
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.ui.components.QuestionTypesCard
import com.orbys.vote.ui.view.fragments.add.AddBooleanQuestion
import com.orbys.vote.ui.view.fragments.add.AddFragment
import com.orbys.vote.ui.view.fragments.add.AddNumericQuestion
import com.orbys.vote.ui.view.fragments.add.AddOtherQuestion
import com.orbys.vote.ui.view.fragments.add.AddStarsQuestion
import com.orbys.vote.ui.view.fragments.add.AddYesNoQuestion
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * Fragmento que contiene los tipos de preguntas que se pueden crear.
 */
@AndroidEntryPoint
class TypesQuestionFragment(private val isNetworkAvaliable: Boolean): Fragment() {

    private lateinit var binding: FragmentQuestionTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionTypesBinding.inflate(inflater, container, false)

        val backButton: ImageButton? = activity?.findViewById(R.id.close_button)
        backButton!!.backButtonFunctions()

        // Detiene los servicios activos
        stopActiveServices()

        with(binding) {

            // Configura las tarjetas de los tipos de preguntas
            yesNoCardContainer.addCardTypeView(AnswerType.YES_NO, AddYesNoQuestion())
            booleanCardContainer.addCardTypeView(AnswerType.BOOLEAN, AddBooleanQuestion())
            starsCardContainer.addCardTypeView(AnswerType.STARS, AddStarsQuestion())
            numericCardContainer.addCardTypeView(AnswerType.NUMERIC, AddNumericQuestion())
            otherCardContainer.addCardTypeView(AnswerType.OTHER, AddOtherQuestion())

            return root
        }

    }

    private fun FrameLayout.addCardTypeView(answerType: AnswerType, fragment: AddFragment) {
        addView(
            QuestionTypesCard(requireContext()).apply {
                bindCard(answerType)
            }
        )

        setOnClickListener {
            if (!isNetworkAvaliable) {
                activity?.showToastWithCustomView(getString(R.string.no_network_error), Toast.LENGTH_LONG)
                return@setOnClickListener
            } else {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    private fun ImageButton.backButtonFunctions() {
        // Detenemos el servicio
        this.setOnClickListener {
            Log.d("DownloadFragment", "ESTA ES LA FUNCION DE CERRAR")
            activity?.finish()
            exitProcess(0)
        }
    }

}