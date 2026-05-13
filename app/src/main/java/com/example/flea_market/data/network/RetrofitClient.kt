package com.example.flea_market.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // 10.0.2.2 — это адрес твоего компа для эмулятора Android
    private const val BASE_URL = "http://10.0.2.2:5170/"

    val instance: FleaMarketApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FleaMarketApi::class.java)
    }
}