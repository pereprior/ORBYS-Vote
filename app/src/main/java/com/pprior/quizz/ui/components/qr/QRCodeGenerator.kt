package com.pprior.quizz.ui.components.qr

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

private const val DEFAULT_SIZE = 200
private const val BLACK = -0x1000000
private const val WHITE = -0x1

class QRCodeGenerator {

    fun encodeAsBitmap(
        url: String,
        width: Int = DEFAULT_SIZE,
        height: Int = DEFAULT_SIZE
    ): Bitmap? {

        // Intenta codificar la URL en un codigo QR
        val result = try {
            MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, null)
        } catch (iae: IllegalArgumentException) {
            // Si falla, devuelve null
            return null
        }

        // Crea un array de píxeles para la imagen Bitmap
        val pixels = IntArray(result.width * result.height) { index ->
            // Asigna las celdas en color blanco y negro como corresponde
            if (result[index % result.width, index / result.width]) BLACK else WHITE
        }

        // Crea la imagen Bitmap con el array de pixeles
        return Bitmap.createBitmap(result.width, result.height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, result.width, 0, 0, result.width, result.height)
        }
    }

}