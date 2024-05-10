package com.orbys.quizz.domain.usecases

import android.content.Context
import com.orbys.quizz.core.managers.NetworkManager
import javax.inject.Inject

// Caso de uso para obtener las credenciales de la red de hotspot
class GetHotspotCredentialsUseCase @Inject constructor(
    private val context: Context, private val networkManager: NetworkManager
) {
    operator fun invoke() = networkManager.getHotspotCredentials(context)
}