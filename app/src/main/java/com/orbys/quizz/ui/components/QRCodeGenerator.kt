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
        private const val BLACK = Color.BLACK
        private const val WHITE = Color.WHITE
        private const val LOGO_SIZE_DIFF = 4
    }

    fun encodeAsBitmap(
        url: String,
        logoResId: Int = R.drawable.orbys_logo,
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
        val qrCode = createBitmapFromBitMatrix(bitMatrix)
        // Convierte el logo a mapa de bits
        val logo = ContextCompat.getDrawable(context, logoResId)?.toBitmap()

        return overlayLogoOnQrCode(qrCode, logo)
    }

    private fun createBitmapFromBitMatrix(bitMatrix: BitMatrix): Bitmap {
        val size = bitMatrix.width
        val blankAreaSize = size / LOGO_SIZE_DIFF
        val blankAreaPosition = (size / 2 - blankAreaSize / 2)..(size / 2 + blankAreaSize / 2)

        val pixels = IntArray(size * size) { index ->
            val x = index % size
            val y = index / size
            // Si la posición está en el área donde se situará el logo, se pone blanco
            if (x in blankAreaPosition && y in blankAreaPosition) WHITE
            // Si no, se pone del color correspondiente para crear el qr
            else if (bitMatrix[x, y]) BLACK else WHITE
        }

        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, size, 0, 0, size, size)
        }
    }

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