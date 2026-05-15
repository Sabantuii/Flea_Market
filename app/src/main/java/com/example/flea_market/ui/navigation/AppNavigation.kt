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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
import com.example.flea_market.data.network.RetrofitClient
import com.example.flea_market.data.repository.AuthRepository
import com.example.flea_market.data.sendPushNotification
import com.example.flea_market.ui.screens.AuthorisationScreen
import com.example.flea_market.ui.screens.BasketScreen
import com.example.flea_market.ui.screens.MainScreen
import com.example.flea_market.ui.screens.NotificationScreen
import com.example.flea_market.ui.screens.ProfileScreen
import com.example.flea_market.ui.screens.RegistrationScreen
import com.example.flea_market.ui.screens.WelcomeScreen
import com.example.flea_market.viewmodels.AuthViewModel
import com.example.flea_market.viewmodels.BasketViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    // 1. Сначала исправляем создание репозитория.
// Ему нужно передать наш Retrofit-клиент (instance)
    val repository = AuthRepository(api = RetrofitClient.instance)

// 2. Создаем ViewModel через Factory.
// Убедись, что AuthViewModelFactory у тебя выглядит примерно так:
    val authViewModel: AuthViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(repository) as T
        }
    })

    val basketViewModel: BasketViewModel = viewModel()

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

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, bottomBar = {
        if (showBottomBar) {
            FleaBottomNavigation(navController = navController)
        }
    }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "welcome",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("main") { MainScreen(navController, basketViewModel) }

            // АВТОРИЗАЦИЯ
            composable("authorisation") {
                AuthorisationScreen(
                    onNavigateToRegistration = { navController.navigate("registration") },
                    onLoginClick = { authResponse ->
                        UserSession.currentUser = authResponse.user

                        navController.navigate("main")
                    })
            }
            // РЕГИСТРАЦИЯ
            composable("registration") {
                RegistrationScreen(
                    onBackToLogin = { navController.navigate("authorisation") },
                    onRegisterClick = { request ->
                        // 1. Сразу сохраняем данные в сессию, чтобы они были доступны в профиле
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

                        // 2. Вызываем реальный запрос на сервер через ViewModel
                        println("!!! Вызываем регистрацию на сервере для: ${request.login}")
                        authViewModel.register(request, request.password)
                    }
                )

                // Следим за состоянием из ViewModel
                val isSuccess by authViewModel.isSuccess
                val error by authViewModel.errorMessage

                // Если сервер ответил "Успех" — летим на главный экран
                LaunchedEffect(isSuccess) {
                    if (isSuccess) {
                        notificationViewModel.addNotification("Регистрация на сервере прошла успешно!")
                        navController.navigate("main") {
                            popUpTo("registration") { inclusive = true }
                        }
                    }
                }

                // Если сервер ругнулся — выводим это в логи (или можно добавить Snackbar)
                LaunchedEffect(error) {
                    if (error != null) {
                        println("!!! Ошибка регистрации на бэкенде: $error")
                    }
                }
            }

            composable("notifications") {
                NotificationScreen(
                    navController = navController, messages = notificationViewModel.notifications
                )
            }

            composable("welcome") {
                WelcomeScreen(onStartClick = { navController.navigate("authorisation") })
            }

            composable("profile") { ProfileScreen(navController) }

            // ЭКРАН КОРЗИНЫ
            composable("basket") {

                BasketScreen(navController, basketViewModel)
            }
        }
    }
}