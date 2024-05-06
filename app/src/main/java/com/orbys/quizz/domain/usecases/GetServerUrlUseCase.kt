package com.orbys.quizz.domain.usecases

import com.orbys.quizz.data.utils.ServerUtils
import javax.inject.Inject

class GetServerUrlUseCase @Inject constructor(
    private val serverUtils: ServerUtils
) {
    operator fun invoke(endpoint: String): String = serverUtils.getServerUrl(endpoint)
}