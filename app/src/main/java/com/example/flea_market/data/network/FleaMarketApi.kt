package com.example.flea_market.data.network

import com.example.flea_market.data.models.AuthResponse
import com.example.flea_market.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FleaMarketApi {
    @POST("/api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
}