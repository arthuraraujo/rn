package com.reactnativeonespanorchestration.data.repository

import com.reactnativeonespanorchestration.data.model.UserRequest
import com.reactnativeonespanorchestration.data.model.UserResponse
import retrofit2.Response

interface OneSpanRepository {
    suspend fun postUser(userRequest: UserRequest): Response<UserResponse>
}
