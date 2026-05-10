package com.example.flea_market

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.example.flea_market.ui.navigation.AppNavigation
import com.example.flea_market.ui.screens.MainScreen
import com.example.flea_market.ui.screens.RegistrationScreen
import com.example.flea_market.ui.screens.WelcomeScreen
import com.example.flea_market.ui.theme.Flea_MarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // В MainActivity.kt внутри onCreate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Вход в систему"
            val descriptionText = "Уведомления об успешной авторизации"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("AUTH_CHANNEL", name, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Flea_MarketTheme {
                AppNavigation()
            }
        }
    }
}