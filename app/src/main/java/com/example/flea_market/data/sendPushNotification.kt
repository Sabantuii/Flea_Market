package com.example.flea_market.data

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.flea_market.R

@SuppressLint("MissingPermission")
fun sendPushNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, "AUTH_CHANNEL")
        .setSmallIcon(R.drawable.ic_notification) // Твоя иконка
        .setContentTitle("Успешный вход")
        .setContentText("Вы успешно вошли в приложение Flea Market!")
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setAutoCancel(true) // Чтобы исчезало после нажатия

    with(NotificationManagerCompat.from(context)) {
        // Уникальный ID для каждого уведомления
        notify(101, builder.build())
    }
}