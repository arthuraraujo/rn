package com.reactnativeonespanorchestration.data.service

import com.google.gson.GsonBuilder
import com.reactnativeonespanorchestration.utils.SessionHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OneSpanServiceImpl {

    fun getService(): OneSpanService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
        }

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(SessionHelper.backendUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build())
            .build()

        return retrofit.create(OneSpanService::class.java)
    }

}
