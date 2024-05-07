package com.orbys.quizz.domain.usecases

import com.orbys.quizz.core.managers.NetworkManager
import javax.inject.Inject

class GetServerUrlUseCase @Inject constructor(
    private val manager: NetworkManager
) {
    operator fun invoke(endpoint: String): String = manager.getServerUrl(endpoint)
}