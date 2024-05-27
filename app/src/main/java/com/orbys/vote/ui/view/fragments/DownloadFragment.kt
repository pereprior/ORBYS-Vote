package com.orbys.vote.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.DOWNLOAD_ENDPOINT
import com.orbys.vote.core.extensions.getAnswersAsString
import com.orbys.vote.core.extensions.getServerUrl
import com.orbys.vote.core.extensions.setExpandOnClick
import com.orbys.vote.core.extensions.showToastWithCustomView
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
            val hotspotUrl = getServerUrl(DOWNLOAD_ENDPOINT, true)
            setQrCode(DOWNLOAD_ENDPOINT, !hotspotUrl.isNullOrEmpty())

            respondContainer.setOnClickListener { setQrCode(DOWNLOAD_ENDPOINT) }
            respondHotspotContainer.setOnClickListener { setQrCode(DOWNLOAD_ENDPOINT, true) }

            return root
        }

    }

    private fun FragmentQrCodeBinding.setQrCode(
        endpoint: String, isHotspot: Boolean = false
    ) {
        val url = getServerUrl(endpoint, isHotspot)

        if (url.isNullOrEmpty()) {
            context?.showToastWithCustomView(requireContext().getString(R.string.no_network_error), Toast.LENGTH_LONG)
            return
        } else {
            val qrGenerator = QRCodeGenerator(requireContext())
            val qrCodeBitmap = qrGenerator.generateUrlQrCode(url, true)

            lanQrCode.apply {
                visibility = if (isHotspot) View.GONE else View.VISIBLE
                setImageBitmap(if (isHotspot) null else qrCodeBitmap)
            }

            lanQrText.apply {
                visibility = if (isHotspot) View.GONE else View.VISIBLE
                text = if (isHotspot) null else url
            }

            hotspotQrText.text = if (isHotspot) url else null
            hotspotQrCode.apply {
                setImageBitmap(if (isHotspot) qrCodeBitmap else null)
                setExpandOnClick()
            }

            otherQrCode.setExpandOnClick()

            step1HotspotContainer.visibility = if (isHotspot) View.VISIBLE else View.GONE
            step2HotspotContainer.visibility = if (isHotspot) View.VISIBLE else View.GONE
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