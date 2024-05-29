package com.orbys.vote.ui.components.qr

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import com.orbys.vote.databinding.DialogQrCodeBinding

/**
 * Clase que extiende de [Dialog] y muestra una imagen ampliada en un dialogo.
 *
 * @param imageDrawable El contenido de la imagen que se va a ampliar.
 * @param size El tamaño de la imagen.
 */
class ImageDialog(
    context: Context, imageDrawable: Drawable, size: Int
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