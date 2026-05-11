package com.example.flea_market.data.models
data class User(
    val login: String,
    val password: String = "",
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val city: String = "",
    val street: String = "",
    val house: String = "",
    val apartment: String = ""
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
//data class AuthResponse(
//    val token: String? = null,
//    val message: String? = null
//)