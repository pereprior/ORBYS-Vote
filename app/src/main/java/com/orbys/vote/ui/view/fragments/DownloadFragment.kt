package com.orbys.vote.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.showToastWithCustomView
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

        val backButton: ImageButton? = activity?.findViewById(R.id.close_button)
        backButton!!.backButtonFunctions()

        // Cambios en la vista del fragmento
        with(binding) {
            var url = viewModel.getServerUrl(DOWNLOAD_ENDPOINT)

            try {
                lanQrCode.setImageBitmap(
                    QRCodeGenerator(requireContext()).generateUrlQrCode(
                        url!!,
                        true
                    )
                )
                lanQrText.text = url
            } catch (e: Exception) {
                requireContext().showToastWithCustomView(
                    requireContext().getString(R.string.no_network_error),
                    Toast.LENGTH_LONG
                )
            }

            // Agregar la vista al FrameLayout
            respondContainer.setOnClickListener {
                try {
                    url = viewModel.getServerUrl(DOWNLOAD_ENDPOINT)

                    lanQrCode.setImageBitmap(
                        QRCodeGenerator(requireContext()).generateUrlQrCode(
                            url!!,
                            true
                        )
                    )
                    lanQrText.text = url

                    lanQrCode.visibility = LinearLayout.VISIBLE
                    lanQrText.visibility = LinearLayout.VISIBLE
                    hotspotQrCode.visibility = LinearLayout.GONE
                    hotspotQrText.visibility = LinearLayout.GONE
                } catch (e: Exception) {
                    requireContext().showToastWithCustomView(
                        requireContext().getString(R.string.no_network_error),
                        Toast.LENGTH_LONG
                    )
                }
            }

            respondHotspotContainer.setOnClickListener {
                try {
                    url = viewModel.getServerUrl(DOWNLOAD_ENDPOINT, true)

                    hotspotQrCode.setImageBitmap(
                        QRCodeGenerator(requireContext()).generateUrlQrCode(
                            url!!,
                            true
                        )
                    )
                    hotspotQrText.text = url

                    lanQrCode.visibility = LinearLayout.GONE
                    lanQrText.visibility = LinearLayout.GONE
                    hotspotQrCode.visibility = LinearLayout.VISIBLE
                    hotspotQrText.visibility = LinearLayout.VISIBLE
                } catch (e: Exception) {
                    requireContext().showToastWithCustomView(
                        requireContext().getString(R.string.no_network_error),
                        Toast.LENGTH_LONG
                    )
                }
            }

            return root
        }

    }

    private fun ImageButton.backButtonFunctions() {
        // Detenemos el servicio
        this.setOnClickListener {
            stopActiveServices()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, TypesQuestionFragment(true))
                addToBackStack(null)
                commit()
            }
        }
    }

}