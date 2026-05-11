package com.example.flea_market.data.repository

import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.network.FleaMarketApi

class AuthRepository(private val api: FleaMarketApi) {

    // Твоя валидация
    fun validateRegistration(data: RegisterRequest, confirmPass: String): String? {
        if (data.login.isBlank() || data.password.isBlank()) return "Заполните все поля"
        if (data.password != confirmPass) return "Пароли не совпадают"
        if (!data.email.contains("@")) return "Неверный формат почты"
        return null
    }

    // Запрос к API
    suspend fun registerUser(data: RegisterRequest) = api.register(data)
}