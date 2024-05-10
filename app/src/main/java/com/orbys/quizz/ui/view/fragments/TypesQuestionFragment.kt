package com.orbys.quizz.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.orbys.quizz.R
import com.orbys.quizz.core.extensions.replaceMainActivityBindingFunctions
import com.orbys.quizz.core.extensions.stopActiveServices
import com.orbys.quizz.databinding.FragmentQuestionTypesBinding
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.view.fragments.cards.BooleanCard
import com.orbys.quizz.ui.view.fragments.cards.NumericCard
import com.orbys.quizz.ui.view.fragments.cards.OtherCard
import com.orbys.quizz.ui.view.fragments.cards.StarsCard
import com.orbys.quizz.ui.view.fragments.cards.YesNoCard
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

            bindHotspotCredentials()

            return root
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