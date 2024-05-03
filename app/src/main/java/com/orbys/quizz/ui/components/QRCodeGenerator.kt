package com.orbys.quizz.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.orbys.quizz.R

/**
 * Clase para generar códigos QR con un logo en el centro.
 *
 * @property context Contexto de la aplicación.
 */
class QRCodeGenerator(private val context: Context) {

    companion object {
        private const val DEFAULT_SIZE = 300
        // Factor por el que se divide el tamaño del qr para obtener el tamaño del logo
        private const val LOGO_SIZE_DIFF = 4
    }

    /**
     * Genera un codigo QR a partir de una URL.
     *
     * @param url URL a codificar.
     * @param logo Indica si se debe añadir un logo en el centro.
     * @param logoResId Identificador del logo.
     * @param width Ancho del codigo QR.
     * @param height Alto del codigo QR.
     * @return Mapa de bits que representa el codigo QR.
     */
    fun encodeAsBitmap(
        url: String,
        logo: Boolean = false,
        logoResId: Int = R.drawable.ic_orbys,
        width: Int = DEFAULT_SIZE,
        height: Int = DEFAULT_SIZE
    ): Bitmap? {
        val bitMatrix = try {
            MultiFormatWriter().encode(
                url,
                BarcodeFormat.QR_CODE,
                width, height,
                // Error correction hace que el qr se pueda seguir leyendo con el logo en el centro
                mapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
            )
        } catch (e: WriterException) {
            return null
        }

        // Crea el mapa de bits del qr
        val qrCode = createBitmapFromBitMatrix(bitMatrix, logo)
        // Convierte el logo a mapa de bits
        val logoBitMap = ContextCompat.getDrawable(context, logoResId)?.toBitmap()

        return overlayLogoOnQrCode(qrCode, logoBitMap)
    }

    /**
     * Crea un mapa de bits a partir de una matriz de bits dada.
     *
     * @param bitMatrix Matriz de bits.
     * @param blankArea Indica si se debe crear un área en blanco en el centro.
     * @return Mapa de bits.
     */
    private fun createBitmapFromBitMatrix(
        bitMatrix: BitMatrix,
        blankArea: Boolean = false
    ): Bitmap {
        val size = bitMatrix.width
        val blankAreaSize = size / LOGO_SIZE_DIFF
        val blankAreaPosition = (size / 2 - blankAreaSize / 2)..(size / 2 + blankAreaSize / 2)

        val pixels = IntArray(size * size) { index ->
            val x = index % size
            val y = index / size
            // Si blankArea es true y la posición está en el área donde se situará el logo, se pone blanco
            if (blankArea && x in blankAreaPosition && y in blankAreaPosition) Color.WHITE
            // Si no, se pone del color correspondiente para crear el qr
            else if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }

        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, size, 0, 0, size, size)
        }
    }

    /**
     * Añade el logo en el centro del código QR.
     *
     * @param qrCode Código QR como mapa de bits.
     * @param logo Logo como mapa de bits.
     * @return Código QR con el logo superpuesto.
     */
    private fun overlayLogoOnQrCode(qrCode: Bitmap, logo: Bitmap?): Bitmap {
        val qrSize = qrCode.width
        val logoSize = qrSize / LOGO_SIZE_DIFF
        val logoPosition = (qrSize - logoSize) / 2f

        // Parametros con los que se dibuja el logo
        val paint = Paint().apply {
            colorFilter = PorterDuffColorFilter(context.getColor(R.color.background_black), PorterDuff.Mode.SRC_IN)
        }

        return Bitmap.createBitmap(qrSize, qrSize, qrCode.config).apply {
            // Dibujamos el qr
            Canvas(this).drawBitmap(qrCode, 0f, 0f, null)
            logo?.let {
                val scaledLogo = Bitmap.createScaledBitmap(it, logoSize, logoSize, false)
                // Dibujamos el logo por encima
                Canvas(this).drawBitmap(scaledLogo, logoPosition, logoPosition, paint)
            }
        }
    }

}