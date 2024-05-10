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
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.orbys.quizz.R

/**
 * Clase para generar códigos QR con un logo en el centro.
 *
 * @property context Contexto de la aplicación.
 */
class QRCodeGenerator(private val context: Context) {

    private val defaultSize = context.resources.getDimensionPixelSize(R.dimen.qr_code_size)
    private val defaultLogoResId = R.drawable.ic_orbys
    // Factor por el que se divide el tamaño del qr para obtener el tamaño del logo
    private val logoSizeDiff = 4

    // Genera un código QR que te conecta a una red wifi
    fun generateWifiQRCode(
        ssid: String, password: String, logo: Boolean = false,
        width: Int = defaultSize, height: Int = defaultSize,
        logoResId: Int = defaultLogoResId
    ): Bitmap {
        val wifiData = "WIFI:S:$ssid;P:$password;T:WPA2;"
        return encodeAsBitmap(wifiData, logo, width, height, logoResId)
    }

    // Genera un código QR que te redirige a una URL en tu navegador
    fun generateUrlQrCode(
        url: String, logo: Boolean = false,
        width: Int = defaultSize, height: Int = defaultSize,
        logoResId: Int = defaultLogoResId
    ): Bitmap {
        return encodeAsBitmap(url, logo, width, height, logoResId)
    }

    /**
     * Codifica un texto en un código QR.
     *
     * @param data Texto a codificar.
     * @param logo Indica si se debe añadir un logo en el centro o no.
     * @param width Ancho del código QR.
     * @param height Alto del código QR.
     * @param logoResId Recurso del logo.
     *
     * @return Código QR como mapa de bits.
     */
    private fun encodeAsBitmap(
        data: String, logo: Boolean, width: Int, height: Int, logoResId: Int
    ): Bitmap {
        val qrCodeWriter = QRCodeWriter()
        val bitMatrix: BitMatrix = qrCodeWriter.encode(
            data, BarcodeFormat.QR_CODE, width, height,
            mapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
        )

        val qrCode = createBitmapFromBitMatrix(bitMatrix, logo)

        return if (logo) {
            val logoBitMap = ContextCompat.getDrawable(context, logoResId)?.toBitmap()
            overlayLogoOnQrCode(qrCode, logoBitMap)
        } else qrCode
    }

    /**
     * Crea un mapa de bits a partir de una matriz de bits dada.
     *
     * @param bitMatrix Matriz de bits.
     * @param blankArea Indica si se debe crear un área en blanco en el centro.
     *
     * @return Mapa de bits.
     */
    private fun createBitmapFromBitMatrix(
        bitMatrix: BitMatrix, blankArea: Boolean = false
    ): Bitmap {
        val size = bitMatrix.width
        val blankAreaSize = size / logoSizeDiff
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
     *
     * @return Código QR con el logo superpuesto.
     */
    private fun overlayLogoOnQrCode(
        qrCode: Bitmap, logo: Bitmap?
    ): Bitmap {
        val qrSize = qrCode.width
        val logoSize = qrSize / logoSizeDiff
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