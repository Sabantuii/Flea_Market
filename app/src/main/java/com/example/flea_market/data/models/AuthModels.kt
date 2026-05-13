package com.example.flea_market.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String?,
    @SerializedName("fullName") val fullName: String?, // Проверь: в Swagger FullName или fullName?
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("street") val street: String?,
    @SerializedName("house") val house: String?,
    @SerializedName("apartment") val apartment: String?
)
// РЕГИСТРАЦИЯ
data class RegisterRequest(
    val login: String,
    val password: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val city: String,
    val street: String,
    val house: String,
    val apartment: String
)
// АВТОРИЗАЦИЯ
data class LoginRequest(
    val login: String,
    val password: String
)

data class AuthResponse(
    val token: String, // Ключ для доступа к другим функциям
    val user: User // Тот самый универсальный класс пользователя со всеми полями
)
