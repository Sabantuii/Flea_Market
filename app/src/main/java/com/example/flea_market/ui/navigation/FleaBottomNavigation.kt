package com.example.flea_market.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flea_market.R
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink

@Composable
fun FleaBottomNavigation(navController: NavController) {
    // 1. Получаем текущий маршрут, чтобы знать, какую иконку подсветить розовым
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(FleaBlue), // Твой синий цвет
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Список ключей экранов (должны совпадать с именами в NavHost)
        val items = listOf("main", "catalog", "orders", "basket", "profile")

        items.forEach { tab ->
            // Если текущий экран в приложении совпадает с этой иконкой — она выбрана
            val isSelected = currentRoute == tab

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) MarketPink else Color.Transparent)
                    .clickable {
                        // 2. Логика перехода при нажатии
                        if (currentRoute != tab) {
                            navController.navigate(tab) {
                                // Очищаем стек, чтобы не плодить копии экранов
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                    .padding(bottom = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                // Используем Image, как в твоем исходном коде
                Image(
                    painter = painterResource(
                        id = when (tab) {
                            "main" -> R.drawable.ic_main
                            "catalog" -> R.drawable.ic_catalog
                            "orders" -> R.drawable.ic_orders
                            "basket" -> R.drawable.ic_basket
                            // Здесь я учел твою правку с названием фото
                            else -> R.drawable.ic_profile
                        }
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun Preview() {
    // Создаем пустой контроллер только для превью
    val navController = rememberNavController()

    FleaBottomNavigation(navController = navController)
}