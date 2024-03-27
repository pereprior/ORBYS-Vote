package com.pprior.quizz.ui.components.utils

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

private const val DEFAULT_SIZE = 200
private const val BLACK = -0x1000000
private const val WHITE = -0x1

/**
 * QRCodeGenerator se encarga de generar los codigos qr.
 *
 * Utiliza la libreria ZXing para codificar una url en un codigo qr y convertir el resultado de una forma legible para el usuario.
 */
class QRCodeGenerator {

    /**
     * Codifica la url y la devuelve como un bitmap.
     *
     * @param url La url que se va a codificar.
     * @param width El ancho de la imagen del qr. Por defecto equivale a DEFAULT_SIZE.
     * @param height El alto de la imagen del qr. Por defecto equivale a DEFAULT_SIZE.
     * @return Un Bitmap que represneta la imagen del qr, o null si la proceso falla.
     */
    fun encodeAsBitmap(
        url: String,
        width: Int = DEFAULT_SIZE,
        height: Int = DEFAULT_SIZE
    ): Bitmap? {
        val bitMatrix = try {
            // Codifica la url en un BitMatrix.
            MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, null)
        } catch (e: IllegalArgumentException) {
            return null
        }

        return createBitmapFromBitMatrix(bitMatrix)
    }

    /**
     * Devuelve un bitmap a partir de un bitmatrix.
     * BitMatrix es una representación 2D del código QR, donde cada bit representa un píxel.
     * Este método convierte BitMatrix en una matriz de píxeles y luego crea un bitmap a partir de esta matriz.
     *
     * @param bitMatrix El BitMatrix a convertir en un bitmap.
     * @return Un mapa de bits que representa el código QR.
     */
    private fun createBitmapFromBitMatrix(bitMatrix: BitMatrix): Bitmap {
        val pixels = IntArray(bitMatrix.width * bitMatrix.height) { index ->
            if (bitMatrix[index % bitMatrix.width, index / bitMatrix.width]) BLACK else WHITE
        }

        return Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, bitMatrix.width, 0, 0, bitMatrix.width, bitMatrix.height)
        }
    }
}