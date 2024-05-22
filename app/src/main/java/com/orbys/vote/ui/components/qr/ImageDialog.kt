package com.orbys.vote.ui.components.qr

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import com.orbys.vote.databinding.DialogQrCodeBinding

class ImageDialog(context: Context, qr: Bitmap): Dialog(context) {

    private var binding: DialogQrCodeBinding = DialogQrCodeBinding.inflate(layoutInflater)

    init {
        with(binding) {
            setContentView(root)
            qrCode.setImageBitmap(qr)
        }

        window?.setLayout(1024, 1024)
    }

}