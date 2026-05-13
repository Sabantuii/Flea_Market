package com.example.flea_market.data.network

import com.example.flea_market.data.models.AuthResponse
import com.example.flea_market.data.models.LoginRequest
import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FleaMarketApi {
    @POST("api/Auth/login") // Проверь путь в Swagger!
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>
}