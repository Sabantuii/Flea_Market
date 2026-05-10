package com.example.flea_market.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flea_market.R
import com.example.flea_market.ui.theme.MarketPink


@Composable
fun NotificationScreen(navController: NavController, messages: List<String>) {
    // 1. Убираем внешний Column с verticalScroll.
    // Вместо него используем Column БЕЗ скролла, чтобы просто разделить шапку и список.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // РОЗОВАЯ ШАПКА (оставляем как есть, она будет зафиксирована сверху)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(MarketPink)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Уведомления",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        // 2. А вот здесь используем LazyColumn, который займет всё оставшееся место
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Это важно! Растягивает список на весь экран
                .padding(horizontal = 16.dp)
        ) {
            // Если нужно добавить отступ сверху перед первым уведомлением
            item { Spacer(Modifier.height(8.dp)) }

            if (messages.isEmpty()) {
                item {
                    Text("Уведомлений пока нет", modifier = Modifier.padding(16.dp))
                }
            } else {
                items(messages) { message ->
                    Text(
                        text = message,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp) // Заменил общий padding на вертикальный для красоты
                            .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun FullPreview() {
    val navController = rememberNavController()

    // Передаем "фейковые" данные для красоты
    NotificationScreen(
        navController = navController,
        messages = listOf(
            "Вы успешно вошли в систему!",
            "Ваш заказ №123 подтвержден",
            "Новое сообщение от продавца"
        )
    )
}