package com.example.flea_market.data

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    // Список всех уведомлений (просто строки для начала)
    val notifications = mutableStateListOf<String>()
    fun addNotification(message: String) {
        notifications.add(0, message) // Добавляем в начало списка
    }
}