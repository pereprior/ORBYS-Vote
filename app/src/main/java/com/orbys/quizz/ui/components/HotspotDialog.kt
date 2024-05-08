package com.orbys.quizz.ui.components

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.orbys.quizz.databinding.FragmentQrCodeBinding

class HotspotDialog(context: Context) : Dialog(context) {

    private lateinit var binding: FragmentQrCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentQrCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val qr = QRCodeGenerator(context)
        val credentials = qr.getHotspotCredentials()

        with(binding) {
            qrCode.setImageBitmap(qr.generateWifiQRCode("SSID", "Password", true))
            qrTitle.text = credentials.first ?: "SSID"

            qrText.visibility = View.VISIBLE
            qrText.text = credentials.second ?: "Password"
        }
    }

}