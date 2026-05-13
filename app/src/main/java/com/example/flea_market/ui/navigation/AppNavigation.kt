package com.example.flea_market.ui.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
// ВАЖНО: Импортируй именно из .compose пакета!
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flea_market.UserSession
import com.example.flea_market.data.NotificationViewModel
import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.models.User
import com.example.flea_market.data.sendPushNotification
import com.example.flea_market.ui.screens.AuthorisationScreen
import com.example.flea_market.ui.screens.MainScreen
import com.example.flea_market.ui.screens.NotificationScreen
import com.example.flea_market.ui.screens.ProfileScreen
import com.example.flea_market.ui.screens.RegistrationScreen
import com.example.flea_market.ui.screens.WelcomeScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val context = androidx.compose.ui.platform.LocalContext.current // ДОБАВИЛИ
    val navController = rememberNavController()
    val notificationViewModel: NotificationViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Лаунчер разрешений
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in listOf("main", "catalog", "orders", "basket", "profile")

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                FleaBottomNavigation(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("main") { MainScreen(navController) }

            // АВТОРИЗАЦИЯ
            composable("authorisation") {
                AuthorisationScreen(
                    onNavigateToRegistration = { navController.navigate("registration") },
                    onLoginClick = { authResponse ->
                        UserSession.currentUser = authResponse.user

                        navController.navigate("main")
                        }
                )
            }
            // РЕГИСТРАЦИЯ
            composable("registration") {
                RegistrationScreen(
                    onBackToLogin = { navController.navigate("authorisation") },
                    onRegisterClick = { request -> // <-- Добавляем прием объекта запроса
                        // 1. СОХРАНЯЕМ ДАННЫЕ В СЕССИЮ (чтобы профиль их увидел)
                        UserSession.currentUser = User(
                            login = request.login,
                            password = request.password,
                            fullName = request.fullName,
                            phone = request.phone,
                            email = request.email,
                            city = request.city,
                            street = request.street,
                            house = request.house,
                            apartment = request.apartment
                        )

                        // 2. Твои уведомления
                        notificationViewModel.addNotification("Добро пожаловать, ${request.login}! Регистрация прошла успешно.")

                        // 3. Переход на главный экран
                        navController.navigate("main") {
                            popUpTo("registration") { inclusive = true } // Чтобы нельзя было вернуться назад на форму
                        }
                    }
                )
            }

            composable("notifications") {
                NotificationScreen(
                    navController = navController,
                    messages = notificationViewModel.notifications
                )
            }

            composable("welcome") {
                WelcomeScreen(onStartClick = { navController.navigate("authorisation") })
            }

            composable("profile") { ProfileScreen(navController) }
        }
    }
}