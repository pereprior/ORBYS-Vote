package com.orbys.vote.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbys.vote.core.extensions.stopActiveServices
import com.orbys.vote.core.managers.NetworkManager.Companion.DOWNLOAD_ENDPOINT
import com.orbys.vote.databinding.FragmentQrCodeBinding
import com.orbys.vote.ui.components.QRCodeGenerator
import com.orbys.vote.ui.viewmodels.QuestionViewModel

/**
 * Fragmento que contiene los elementos para descargar el fichero con los resultados de la pregunta.
 */
class DownloadFragment(
    private val viewModel: QuestionViewModel
): Fragment() {

    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        // Detenemos los servicios activos
        stopActiveServices(true)

        // Cambios en la vista del fragmento
        with(binding) {
            val url = viewModel.getServerUrl(DOWNLOAD_ENDPOINT)

            qrCode.setImageBitmap(QRCodeGenerator(requireContext())
                .generateUrlQrCode(url ?: "", true))
            qrText.text = url

            return root
        }

    }

}