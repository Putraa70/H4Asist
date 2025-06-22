// com/example/uastam/BackendApi.kt
package com.example.uastam

import retrofit2.Call
import retrofit2.http.*


interface BackendApi {
    @POST("register")
    fun register(@Body req: RegisterRequest): Call<RegisterResponse>

    @POST("login")
    fun login(@Body req: LoginRequest): Call<LoginResponse>

    @POST("chat")
    fun chat(@Body req: ChatRequest): Call<ChatResponse>

    @GET("history/{username}")
    fun history(@Path("username") username: String): Call<List<HistoryItem>>
}
