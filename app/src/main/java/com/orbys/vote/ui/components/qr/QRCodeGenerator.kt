package com.orbys.vote.ui.components.qr

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
import com.orbys.vote.R

/** Clase para generar códigos QR de manera personalizada */
class QRCodeGenerator(private val context: Context) {

    fun generateWifiQRCode(
        ssid: String, password: String,
        width: Int = context.resources.getDimensionPixelSize(R.dimen.large_qr_code_size), height: Int = context.resources.getDimensionPixelSize(R.dimen.large_qr_code_size)
    ): Bitmap {
        //"WIFI:S:SSID;P:PASSWORD;T:Security;"
        val wifiData = "WIFI:S:$ssid;P:$password;T:WPA2;"
        val bitMatrix = encodeAsBitmap(wifiData, width, height)
        return createBitmapFromBitMatrix(bitMatrix)
    }

    /**
     * Genera un código QR a partir de una URL.
     *
     * @param url URL a codificar.
     * @param qrWithLogo Variable para indicar si se quiere generar el código qr con una imagen en el centro.
     * @param logoResId Identificador del recurso del logo (si [qrWithLogo] es false, esta variable no hará nada).
     * @param width Ancho del código QR.
     * @param height Alto del código QR.
     */
    fun generateUrlQrCode(
        url: String, qrWithLogo: Boolean = false, logoResId: Int = R.drawable.ic_orbys,
        width: Int = context.resources.getDimensionPixelSize(R.dimen.large_qr_code_size), height: Int = context.resources.getDimensionPixelSize(R.dimen.large_qr_code_size)
    ): Bitmap {
        val bitMatrix = encodeAsBitmap(url, width, height)
        val qrCode = createBitmapFromBitMatrix(bitMatrix, qrWithLogo)

        return if (qrWithLogo) {
            val logoBitMap = ContextCompat.getDrawable(context, logoResId)?.toBitmap()
            overlayLogoOnQrCode(qrCode, logoBitMap)
        } else qrCode
    }

    /**
     * Códifica una cadena de strings a una matriz de bits.
     *
     * @param data Cadena de strings a codificar.
     * @param width Ancho de la matriz de bits.
     * @param height Alto de la matriz de bits.
     */
    private fun encodeAsBitmap(
        data: String, width: Int, height: Int
    ): BitMatrix  = QRCodeWriter().encode(
        data, BarcodeFormat.QR_CODE, width, height,
        // Corrección para que el BitMatrix sea legible incluso con areas en blanco
        mapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
    )

    /**
     * Crea un mapa de bits a partir de una matriz de bits.
     *
     * @param bitMatrix Matriz de bits a convertir.
     * @param witBlankArea Indica si se debe crear un área en blanco en el centro para introducir un logo.
     */
    private fun createBitmapFromBitMatrix(
        bitMatrix: BitMatrix, witBlankArea: Boolean = false
    ): Bitmap {
        val size = bitMatrix.width
        val blankAreaSize = size / LOGO_SIZE_DIFF
        val blankAreaPosition = (size / 2 - blankAreaSize / 2)..(size / 2 + blankAreaSize / 2)

        val pixels = IntArray(size * size) { index ->
            val x = index % size
            val y = index / size
            // Si blankArea es true y la posición está en el área donde se situará el logo, se pone blanco
            if (witBlankArea && x in blankAreaPosition && y in blankAreaPosition) Color.WHITE
            // Si no, se pone del color correspondiente para crear el qr
            else if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
        }

        return Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, size, 0, 0, size, size)
        }
    }

    /**
     * Modifica el mapa de bits del código QR para superponer un logo.
     *
     * @param qrCode Código QR sin el logo.
     * @param logo Logo como mapa de bits.
     */
    private fun overlayLogoOnQrCode(
        qrCode: Bitmap, logo: Bitmap?
    ): Bitmap {
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

    private companion object {
        const val LOGO_SIZE_DIFF = 4
    }

}