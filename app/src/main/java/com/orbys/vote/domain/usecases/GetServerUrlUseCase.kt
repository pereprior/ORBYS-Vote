package com.orbys.vote.domain.usecases

import com.orbys.vote.core.managers.NetworkManager
import javax.inject.Inject

class GetServerUrlUseCase @Inject constructor(
    private val manager: NetworkManager
) {
    operator fun invoke(endpoint: String, isHotspot: Boolean = false) = manager.getServerWifiUrl(endpoint, isHotspot)
}