package com.example.flea_market.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:5170/"

    // 1. Создаем перехватчик (интерцептор) логов
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 2. Создаем клиент и добавляем в него логи
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // 3. Создаем ОДИН объект Retrofit с этим клиентом
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client) // Привязываем клиент с логами
        .build()

    // 4. Генерируем реализацию API
    val instance: FleaMarketApi by lazy {
        retrofit.create(FleaMarketApi::class.java)
    }
}