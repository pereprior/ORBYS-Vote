package com.orbys.vote.domain.usecases

import com.orbys.vote.data.services.HttpService
import javax.inject.Inject

/** Caso de uso que retorna el servicio que controla el servidor http */
class GetHttpServiceUseCase @Inject constructor(
    private val service: HttpService
) {
    operator fun invoke() = service
}