package com.example.flea_market.utils

object AuthValidator {
    // 1. Валидация почты (строго домены и длина больше 10)
    fun validateEmail(email: String): String? {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(gmail\\.com|mail\\.ru)$".toRegex()
        return when {
            email.length < 10 -> "Почта должна быть не короче 10 символов"
            !email.matches(emailRegex) -> "Разрешены только @gmail.com или @mail.ru"
            else -> null
        }
    }

    // 2. Валидация логина и ФИО (минимум 8 символов)
    fun validateMinLength(text: String, fieldName: String): String? {
        return if (text.trim().length < 8)
            "$fieldName должен быть не меньше 8 символов"
        else null
    }

    // 3. Проверка на пустые поля (для адреса)
    fun validateNotEmpty(text: String, fieldName: String): String? {
        return if (text.trim().isEmpty())
            "Заполните поле: $fieldName"
        else null
    }

    // Остальные методы без изменений
    fun validatePhone(phone: String): String? {
        val digits = phone.filter { it.isDigit() }
        return if (digits.length != 10) "Введите полный номер телефона" else null
    }

    fun validatePassword(pass: String): String? {
        return if (pass.length < 6) "Пароль должен быть от 6 символов" else null
    }

    fun validatePasswordsMatch(pass: String, repeat: String): String? {
        return if (pass != repeat) "Пароли не совпадают" else null
    }
}