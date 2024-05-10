package com.orbys.quizz.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.orbys.quizz.databinding.FragmentQrCodeBinding
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Dialogo que amplia la vista del codigo qr para conectarse a la red.
 */
@AndroidEntryPoint
class HotspotDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private lateinit var viewModel: QuestionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        val qr = QRCodeGenerator(requireContext())
        val credentials = viewModel.getHotspotCredentials()

        with(binding) {
            qrCode.setImageBitmap(qr.generateWifiQRCode("123456789", "Hola01", true))
            qrTitle.text = credentials.first ?: "123456789"

            qrText.visibility = View.VISIBLE
            qrText.text = credentials.second ?: "Hola01"
        }

        return binding.root
    }

}