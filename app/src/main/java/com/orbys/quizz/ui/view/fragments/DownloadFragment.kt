package com.orbys.quizz.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.orbys.quizz.databinding.FragmentDownloadBinding
import com.orbys.quizz.ui.view.components.QRCodeGenerator
import com.orbys.quizz.ui.services.FloatingViewService

class DownloadFragment: Fragment() {

    private lateinit var binding: FragmentDownloadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadBinding.inflate(inflater, container, false)

        with(binding) {
            qrCode.setImageBitmap(QRCodeGenerator().encodeAsBitmap("https://google.com"))

            closeButton.setOnClickListener {
                context?.startService(Intent(context, FloatingViewService::class.java))

                activity?.finish()
            }

            return root
        }

    }

}