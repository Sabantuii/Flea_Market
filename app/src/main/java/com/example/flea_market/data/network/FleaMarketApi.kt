package com.example.flea_market.data.network

import com.example.flea_market.data.Product
import com.example.flea_market.data.models.AuthResponse
import com.example.flea_market.data.models.LoginRequest
import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FleaMarketApi {
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    // ДОБАВЬ ЭТО:
    @GET("api/Products")
    suspend fun getProducts(): Response<List<Product>>
}