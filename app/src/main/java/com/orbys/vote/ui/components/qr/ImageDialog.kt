package com.orbys.vote.ui.components.qr

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import com.orbys.vote.databinding.DialogQrCodeBinding

class ImageDialog(
    context: Context,
    imageDrawable: Drawable,
    size: Int
): Dialog(context) {

    private var binding: DialogQrCodeBinding = DialogQrCodeBinding.inflate(layoutInflater)

    init {
        with(binding) {
            setContentView(root)
            qrCode.setImageDrawable(imageDrawable)
        }

        window?.setLayout(size, size)
    }

}