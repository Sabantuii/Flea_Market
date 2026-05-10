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
import com.example.flea_market.data.NotificationViewModel
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

            composable("authorisation") {
                AuthorisationScreen(
                    onNavigateToRegistration = { navController.navigate("registration") },
                    onLoginClick = { // ДОБАВИЛИ ЭТОТ БЛОК
                        notificationViewModel.addNotification("Вы успешно авторизовались!")

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                        sendPushNotification(context)

                        navController.navigate("main")
                    }
                )
            }

            composable("registration") {
                RegistrationScreen(
                    onBackToLogin = { navController.navigate("authorisation") },
                    onRegisterClick = {
                        notificationViewModel.addNotification("Добро пожаловать! Регистрация прошла успешно.")
                        // Аналогично можно добавить пуш и тут
                        navController.navigate("main")
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