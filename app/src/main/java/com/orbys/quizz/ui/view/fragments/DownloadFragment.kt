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
import com.orbys.quizz.data.utils.ServerUtils.Companion.DOWNLOAD_ENDPOINT
import com.orbys.quizz.databinding.FragmentQrCodeBinding
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.viewmodels.QuestionViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragmento que contiene los elementos para descargar el fichero con los resultados de la pregunta.
 */
@AndroidEntryPoint
class DownloadFragment: Fragment() {

    private lateinit var viewModel: QuestionViewModel
    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[QuestionViewModel::class.java]
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)

        // Detenemos los servicios activos
        stopActiveServices(true)

        // Cambios en los elementos de la actividad principal
        replaceMainActivityBindingFunctions(
            titleRedId = R.string.download_title,
            backButtonVisibility = View.VISIBLE,
            backButtonNavFragment = TypesQuestionFragment()
        )

        // Cambios en la vista del fragmento
        with(binding) {
            val url = viewModel.getServerUrl(DOWNLOAD_ENDPOINT)

            qrCode.setImageBitmap(QRCodeGenerator(requireContext())
                .encodeAsBitmap(url, true))

            qrUrl.text = url

            return root
        }

    }

}