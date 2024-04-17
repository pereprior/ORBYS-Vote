package com.orbys.quizz.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbys.quizz.data.constants.DOWNLOAD_ENDPOINT
import com.orbys.quizz.data.constants.SERVER_PORT
import com.orbys.quizz.data.constants.URL_ENTRY
import com.orbys.quizz.data.constants.hostIP
import com.orbys.quizz.databinding.FragmentDownloadBinding
import com.orbys.quizz.ui.view.components.QRCodeGenerator
import com.orbys.quizz.ui.services.FloatingViewService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class DownloadFragment: Fragment() {

    private lateinit var binding: FragmentDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)
        val url = "$URL_ENTRY$hostIP:$SERVER_PORT$DOWNLOAD_ENDPOINT"

        with(binding) {
            qrCode.setImageBitmap(
                QRCodeGenerator().encodeAsBitmap(url)
            )

            closeButton.setOnClickListener {
                context?.startService(Intent(context, FloatingViewService::class.java))

                activity?.finish()
            }

            return root
        }

    }

}