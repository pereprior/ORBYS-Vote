package com.orbys.vote.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.orbys.vote.R
import com.orbys.vote.core.extensions.DOWNLOAD_ENDPOINT
import com.orbys.vote.core.extensions.getAnswersAsString
import com.orbys.vote.core.extensions.replaceFragmentOnClick
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.databinding.FragmentQrCodeBinding
import com.orbys.vote.ui.components.qr.setQrOptions
import com.orbys.vote.ui.viewmodels.QuestionViewModel

/**
 * Fragmento que contiene los elementos para descargar el fichero con los resultados de la pregunta.
 *
 * @property viewModel Contiene los datos de las preguntas las funciones para poder gestionarlas
 */
class DownloadFragment(private val viewModel: QuestionViewModel): Fragment() {

    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        // Modificamos la línea del fichero de respuestas por si se han añadido nuevas respuestas tras la creación del fichero
        val answersFileContent = "Answers;${viewModel.getQuestion().getAnswersAsString().joinToString(";")}"
        viewModel.modifyFile(2, answersFileContent)

        val backButton: ImageView = activity?.findViewById(R.id.close_button)!!
        replaceFragmentOnClick(backButton, TypesQuestionFragment(viewModel))

        with(binding) {
            setQrOptions(requireContext(), DOWNLOAD_ENDPOINT, getString(R.string.step_3_hotspot_text)) {
                requireContext().showToastWithCustomView(getString(R.string.no_hotspot_error))
            }
            return root
        }

    }

}