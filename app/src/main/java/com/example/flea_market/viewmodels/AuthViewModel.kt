package com.example.flea_market.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Состояние: идет ли сейчас загрузка?
    var isLoading = mutableStateOf(false)
    // Состояние: текст ошибки, если что-то пошло не так
    var errorMessage = mutableStateOf<String?>(null)
    // Состояние: успешно ли прошла регистрация?
    var isSuccess = mutableStateOf(false)

    fun register(data: RegisterRequest, confirmPass: String) {
        // 1. Сначала валидация
        val validationError = repository.validateRegistration(data, confirmPass)
        if (validationError != null) {
            errorMessage.value = validationError
            return
        }

        // 2. Если всё ок, шлем запрос
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null

            try {
                val response = repository.registerUser(data)
                if (response.isSuccessful) {
                    isSuccess.value = true
                } else {
                    errorMessage.value = "Ошибка: ${response.code()}"
                }
            } catch (e: Exception) {
                errorMessage.value = "Нет связи с сервером"
            } finally {
                isLoading.value = false
            }
        }
    }
}