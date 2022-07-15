package com.reactnativeonespanorchestration.data.repository

import com.reactnativeonespanorchestration.data.model.UserRequest
import com.reactnativeonespanorchestration.data.service.OneSpanService

class OneSpanRepositoryImpl(private val service: OneSpanService): OneSpanRepository {

    override suspend fun postUser(userRequest: UserRequest) = service.postUser(userRequest)

}
