package com.reactnativeonespanorchestration.data.service

import com.reactnativeonespanorchestration.data.model.UserRequest
import com.reactnativeonespanorchestration.data.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OneSpanService {

    @POST("v1/users/register")
    suspend fun postUser(@Body userRequest: UserRequest): Response<UserResponse>

}
