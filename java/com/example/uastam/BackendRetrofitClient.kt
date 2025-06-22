package com.example.uastam

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BackendRetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

    fun getClient(): BackendApi {
        val client = OkHttpClient.Builder().build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BackendApi::class.java)
    }
}
