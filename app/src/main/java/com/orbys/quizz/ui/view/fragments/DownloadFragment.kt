package com.orbys.quizz.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.data.utils.ServerUtils
import com.orbys.quizz.data.utils.ServerUtils.Companion.DOWNLOAD_ENDPOINT
import com.orbys.quizz.databinding.FragmentQrCodeBinding
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.services.FloatingViewService

/**
 * Fragmento que contiene los elementos para descargar el fichero con los resultados de la pregunta.
 */
class DownloadFragment: Fragment() {

    private lateinit var binding: FragmentQrCodeBinding
    private val serverUtils = ServerUtils()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrCodeBinding.inflate(inflater, container, false)
        val url = serverUtils.getServerUrl(DOWNLOAD_ENDPOINT)
        activity?.stopService(Intent(activity, FloatingViewService::class.java))

        with(binding) {
            val backButton: ImageButton = activity?.findViewById(R.id.back_button) ?: return root
            val closeButton: ImageButton = activity?.findViewById(R.id.close_button) ?: return root
            val title: TextView = activity?.findViewById(R.id.title) ?: return root

            title.text = getString(R.string.download_title)
            closeButton.visibility = View.GONE
            backButton.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fragment_container, TypesQuestionFragment())
                    commit()
                }
            }

            qrCode.setImageBitmap(QRCodeGenerator(requireContext()).encodeAsBitmap(url, true))
            qrUrl.text = url

            return root
        }

    }

}