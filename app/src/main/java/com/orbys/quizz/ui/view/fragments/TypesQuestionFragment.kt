package com.orbys.quizz.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.stopActiveServices
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.domain.models.AnswerType
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.components.QuestionTypesCard
import com.orbys.quizz.ui.view.fragments.add.AddBooleanQuestion
import com.orbys.quizz.ui.view.fragments.add.AddFragment
import com.orbys.quizz.ui.view.fragments.add.AddNumericQuestion
import com.orbys.quizz.ui.view.fragments.add.AddOtherQuestion
import com.orbys.quizz.ui.view.fragments.add.AddStarsQuestion
import com.orbys.quizz.ui.view.fragments.add.AddYesNoQuestion
import com.orbys.quizz.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento que contiene los tipos de preguntas que se pueden crear.
 */
@AndroidEntryPoint
class TypesQuestionFragment: Fragment() {

    private lateinit var binding: FragmentQuestionTypesBinding
    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        binding = FragmentQuestionTypesBinding.inflate(inflater, container, false)

        // Detiene los servicios activos
        stopActiveServices()

        with(binding) {

            // Configura las tarjetas de los tipos de preguntas
            yesNoCardContainer.addCardTypeView(AnswerType.YES_NO, AddYesNoQuestion())
            booleanCardContainer.addCardTypeView(AnswerType.BOOLEAN, AddBooleanQuestion())
            starsCardContainer.addCardTypeView(AnswerType.STARS, AddStarsQuestion())
            numericCardContainer.addCardTypeView(AnswerType.NUMERIC, AddNumericQuestion())
            otherCardContainer.addCardTypeView(AnswerType.OTHER, AddOtherQuestion())

            bindHotspotCredentials()

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
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun FragmentQuestionTypesBinding.bindHotspotCredentials() {
        val credentials = viewModel.getHotspotCredentials()
        val ssid = credentials.first ?: "123456789"
        val password = credentials.second ?: "Hola01"

        val qrSize = context?.resources?.getDimensionPixelSize(R.dimen.micro_qr_code_size) ?: 0

        hotspotQrSsid.text = ssid
        hotspotQrPassword.text = password
        hotspotQr.setImageBitmap(
            QRCodeGenerator(requireContext())
                .generateWifiQRCode(ssid, password, false, qrSize, qrSize)
        )

        hotspotQr.setOnClickListener {
            HotspotDialogFragment().show(parentFragmentManager, "HotspotDialog")
        }
    }

}