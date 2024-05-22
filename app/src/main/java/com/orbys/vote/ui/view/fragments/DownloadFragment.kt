package com.orbys.vote.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.getAnswersAsString
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.core.managers.NetworkManager.Companion.DOWNLOAD_ENDPOINT
import com.orbys.vote.databinding.FragmentQrCodeBinding
import com.orbys.vote.ui.components.qr.QRCodeGenerator
import com.orbys.vote.ui.viewmodels.QuestionViewModel

/**
 * Fragmento que contiene los elementos para descargar el fichero con los resultados de la pregunta.
 */
class DownloadFragment(private val viewModel: QuestionViewModel): Fragment() {

    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        val answersFileContent = "Answers;${viewModel.getQuestion().getAnswersAsString().joinToString(";")}"
        viewModel.modifyFile(2, answersFileContent)

        val backButton: ImageButton? = activity?.findViewById(R.id.close_button)
        backButton!!.backButtonFunctions()

        // Cambios en la vista del fragmento
        with(binding) {
            setQrCode(DOWNLOAD_ENDPOINT)
            respondContainer.setOnClickListener { setQrCode(DOWNLOAD_ENDPOINT) }
            respondHotspotContainer.setOnClickListener { setQrCode(DOWNLOAD_ENDPOINT, true) }

            return root
        }

    }

    private fun FragmentQrCodeBinding.setQrCode(
        endpoint: String, isHotspot: Boolean = false
    ) {
        try {
            val url = viewModel.getServerUrl(endpoint, isHotspot)!!
            val qrCodeBitmap = QRCodeGenerator(requireContext()).generateUrlQrCode(url, true)

            lanQrCode.apply {
                visibility = if (isHotspot) View.GONE else View.VISIBLE
                setImageBitmap(if (isHotspot) null else qrCodeBitmap)
            }

            lanQrText.apply {
                visibility = if (isHotspot) View.GONE else View.VISIBLE
                text = if (isHotspot) null else url
            }

            hotspotQrCode.apply {
                visibility = if (isHotspot) View.VISIBLE else View.GONE
                setImageBitmap(if (isHotspot) qrCodeBitmap else null)
            }

            hotspotQrText.apply {
                visibility = if (isHotspot) View.VISIBLE else View.GONE
                text = if (isHotspot) url else null
            }
        } catch (e: Exception) {
            requireContext().showToastWithCustomView(requireContext().getString(R.string.no_network_error), Toast.LENGTH_LONG)
        }

    }

    private fun ImageButton.backButtonFunctions() {
        // Detenemos el servicio
        this.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, TypesQuestionFragment(viewModel))
                addToBackStack(null)
                commit()
            }
        }
    }

}