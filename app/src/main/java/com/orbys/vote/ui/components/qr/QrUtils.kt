package com.orbys.vote.ui.components.qr

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.wifi.WifiManager
import android.view.View
import android.widget.Toast
import com.orbys.vote.R
import com.orbys.vote.core.extensions.getServerUrl
import com.orbys.vote.core.extensions.setExpandOnClick
import com.orbys.vote.core.extensions.showToastWithCustomView
import com.orbys.vote.databinding.DialogQrCodeBinding
import com.orbys.vote.databinding.FragmentQrCodeBinding
import java.lang.reflect.InvocationTargetException

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

/**
 * Esta función gestiona las opciones del qr que se muestran en la vista flotante
 * Comprobamos si hay algúna IP como punto de acceso, si no la hay solo mostramos el qr para la red local
 *
 * @param context Contexto de la aplicación
 * @param endpoint Indica el endpoint al que te dirige la url del código qr
 * @param hotspotStep2Text Texto que se muestra en el paso 2 del qr para la red de punto de acceso
 */
fun FragmentQrCodeBinding.setQrOptions(
    context: Context, endpoint: String, hotspotStep2Text: String = context.getString(R.string.step_2_hotspot_text)
) {
    val hotspotUrl = getServerUrl(endpoint, true)
    // Mostramos el qr por defecto al abrir el widget
    setQrCode(context, endpoint, hotspotStep2Text, !hotspotUrl.isNullOrEmpty())

    // Al pulsar a cada uno de los contenedores, se muestra el qr correspondiente
    respondContainer.setOnClickListener { setQrCode(context, endpoint, hotspotStep2Text) }
    respondHotspotContainer.setOnClickListener { setQrCode(context, endpoint, hotspotStep2Text, true) }
}

/**
 * Esta función modifica la vista del qr en función de si se debe mostrar el qr para la red local o para la red de punto de acceso
 *
 * @param isHotspot Indica si se debe mostrar el qr para hotspot
 * @param endpoint Indica el endpoint al que te dirige la url del código qr
 * @param hotspotStep2Text Texto que se muestra en el paso 2 del qr para la red de punto de acceso
 */
private fun FragmentQrCodeBinding.setQrCode(
    context: Context, endpoint: String, hotspotStep2Text: String, isHotspot: Boolean = false
) {
    val url = getServerUrl(endpoint, isHotspot)
    // Si la url es nula o vacía, mostramos un mensaje de error
    if (url.isNullOrEmpty()) {
        context.showToastWithCustomView(context.getString(R.string.no_network_error), Toast.LENGTH_LONG)
        return
    }

    val qrGenerator = QRCodeGenerator(context)
    val qrCodeBitmap = qrGenerator.generateUrlQrCode(url, true)

    if (isHotspot) {
        // Qr para la red de punto de acceso
        lanQrCode.visibility = View.GONE
        lanQrText.visibility = View.GONE
        step1HotspotContainer.visibility = View.VISIBLE
        step2HotspotContainer.visibility = View.VISIBLE
        step2HotspotText.text = hotspotStep2Text

        hotspotQrText.text = url
        otherQrCode.setExpandOnClick()
        hotspotQrCode.apply {
            setImageBitmap(qrCodeBitmap)
            setExpandOnClick()
        }
    } else {
        // Qr para la red local
        step1HotspotContainer.visibility = View.GONE
        step2HotspotContainer.visibility = View.GONE

        lanQrCode.apply {
            visibility = View.VISIBLE
            setImageBitmap(qrCodeBitmap)
        }
        lanQrText.apply {
            visibility = View.VISIBLE
            text = url
        }
    }
}

fun getHotspotCredentials(context: Context): Pair<String?, String?> {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    try {
        val method = wifiManager.javaClass.getDeclaredMethod("getWifiApConfiguration")
        method.isAccessible = true
        val wifiConfig = method.invoke(wifiManager) as Any
        val ssidField = wifiConfig.javaClass.getDeclaredField("SSID")
        ssidField.isAccessible = true
        val ssid = ssidField.get(wifiConfig) as String
        val passwordField = wifiConfig.javaClass.getDeclaredField("preSharedKey")
        passwordField.isAccessible = true
        val password = passwordField.get(wifiConfig) as String
        return Pair(ssid.removeSurrounding("\""), password)
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }
    return Pair(null, null)
}