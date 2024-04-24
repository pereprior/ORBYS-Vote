package com.orbys.quizz.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.orbys.quizz.R
import com.orbys.quizz.data.utils.ServerUtils
import com.orbys.quizz.data.utils.ServerUtils.Companion.DOWNLOAD_ENDPOINT
import com.orbys.quizz.databinding.FragmentDownloadBinding
import com.orbys.quizz.ui.components.QRCodeGenerator
import com.orbys.quizz.ui.view.activities.MainActivity
import kotlin.system.exitProcess

/**
 * Fragmento que contiene los elementos para descargar el fichero con los resultados de la pregunta.
 */
class DownloadFragment: Fragment() {

    private lateinit var binding: FragmentDownloadBinding
    private val serverUtils = ServerUtils()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)
        val url = serverUtils.getServerUrl(DOWNLOAD_ENDPOINT)

        with(binding) {
            val backButton: ImageButton = activity?.findViewById(R.id.back_button) ?: return root
            val closeButton: ImageButton = activity?.findViewById(R.id.close_button) ?: return root

            backButton.setOnClickListener {
                activity?.finish()
                val intent = Intent(context, MainActivity::class.java)
                context?.startActivity(intent)
            }
            closeButton.setOnClickListener {
                activity?.finish()
                exitProcess(0)
            }

            qrCode.setImageBitmap(QRCodeGenerator(requireContext()).encodeAsBitmap(url, R.drawable.orbys_logo))
            qrUrl.text = url

            return root
        }

    }

}