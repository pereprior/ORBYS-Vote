package com.orbys.vote.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.data.services.HttpService
import com.orbys.vote.databinding.FragmentQuestionTypesBinding
import com.orbys.vote.domain.models.AnswerType
import com.orbys.vote.ui.components.QuestionTypesCard
import com.orbys.vote.ui.services.FloatingViewService
import com.orbys.vote.ui.view.fragments.add.AddBooleanQuestion
import com.orbys.vote.ui.view.fragments.add.AddFragment
import com.orbys.vote.ui.view.fragments.add.AddNumericQuestion
import com.orbys.vote.ui.view.fragments.add.AddOtherQuestion
import com.orbys.vote.ui.view.fragments.add.AddStarsQuestion
import com.orbys.vote.ui.view.fragments.add.AddYesNoQuestion
import com.orbys.vote.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

/**
 * Fragmento que contiene los tipos de preguntas que se pueden crear.
 */
@AndroidEntryPoint
class TypesQuestionFragment(private val viewModel: QuestionViewModel): Fragment() {

    private lateinit var binding: FragmentQuestionTypesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionTypesBinding.inflate(inflater, container, false)

        val backButton: ImageButton? = activity?.findViewById(R.id.close_button)
        backButton!!.backButtonFunctions()

        // Detiene los servicios activos
        activity?.stopActiveServices()

        with(binding) {

            // Configura las tarjetas de los tipos de preguntas
            yesNoCardContainer.addCardTypeView(AnswerType.YES_NO, AddYesNoQuestion(viewModel))
            booleanCardContainer.addCardTypeView(AnswerType.BOOLEAN, AddBooleanQuestion(viewModel))
            starsCardContainer.addCardTypeView(AnswerType.STARS, AddStarsQuestion(viewModel))
            numericCardContainer.addCardTypeView(AnswerType.NUMERIC, AddNumericQuestion(viewModel))
            otherCardContainer.addCardTypeView(AnswerType.OTHER, AddOtherQuestion(viewModel))

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
            if (!viewModel.isNetworkAvailable(activity as AppCompatActivity)) {
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
            activity?.finish()
            exitProcess(0)
        }
    }

    // Cierra los servicios activos de la actividad
    private fun FragmentActivity.stopActiveServices() {
        this.stopService(Intent(this, HttpService::class.java))
        this.stopService(Intent(this, FloatingViewService::class.java))
    }

}