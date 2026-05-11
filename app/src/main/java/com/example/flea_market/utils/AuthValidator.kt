package com.example.flea_market.utils

object AuthValidator {
    fun validatePhone(phone: String): String? {
        val digits = phone.filter { it.isDigit() }
        return if (digits.length != 10) "Введите полный номер телефона" else null
    }

    fun validateEmail(email: String): String? {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$".toRegex()
        return if (!email.matches(emailRegex)) "Некорректный формат почты" else null
    }

    fun validatePassword(pass: String): String? {
        return if (pass.length < 6) "Пароль должен быть от 6 символов" else null
    }

    fun validatePasswordsMatch(pass: String, repeat: String): String? {
        return if (pass != repeat) "Пароли не совпадают" else null
    }
}