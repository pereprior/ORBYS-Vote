package com.orbys.quizz.domain.usecases

import com.orbys.quizz.data.services.HttpService
import javax.inject.Inject

// Caso de uso que retorna el servicio de red
class GetHttpServiceUseCase @Inject constructor(
    private val service: HttpService
) {
    operator fun invoke() = service
}