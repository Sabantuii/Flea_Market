package com.example.flea_market.ui.screens

import android.R.attr.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flea_market.R
import com.example.flea_market.data.Product
import com.example.flea_market.data.ProductCard
import com.example.flea_market.ui.navigation.FleaBottomNavigation
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {

// Состояния для поиска
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("home") }

    val products = remember {
        listOf(
            Product(
                1,
                "12.990 ₽",
                "Kingston Fury DDR5 32GB 6000MHz",
                R.drawable.culer
            ),
            Product(2, "61.430 ₽", "MSI SPATIUM M.2 SSD 2TB", R.drawable.ic_launcher_background),
            Product(
                3,
                "17.930 ₽",
                "Intel Core i5 12400F LGA1700",
                R.drawable.ic_launcher_background
            ),
            Product(
                3,
                "17.930 ₽",
                "Intel Core i5 12400F LGA1700",
                R.drawable.ic_launcher_background
            ),
            Product(
                3,
                "17.930 ₽",
                "Intel Core i5 12400F LGA1700",
                R.drawable.ic_launcher_background
            ),
            Product(
                3,
                "17.930 ₽",
                "Intel Core i5 12400F LGA1700",
                R.drawable.ic_launcher_background
            ),
            Product(
                3,
                "17.930 ₽",
                "Intel Core i5 12400F LGA1700",
                R.drawable.ic_launcher_background
            ),
            Product(4, "35.200 ₽", "Gigabyte RTX 4060 8GB", R.drawable.ic_launcher_background)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F2)) // Тот самый серый фон
    ) {
        // --- СИНЯЯ ШАПКА (Высота 200) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FleaBlue) // Тот самый синий цвет
                .statusBarsPadding() // ДОБАВЛЯЕМ ЭТО: текст не налезет на время
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            Column {
                Text(
                    text = "Flea Market",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Создаем свою строку, чтобы отступы нас не ограничивали
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp), // Теперь эта высота честная
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier.fillMaxSize(),
                            textStyle = TextStyle(
                                fontSize = 18.sp,
                                color = Color.Black,
                                platformStyle = PlatformTextStyle(includeFontPadding = false) // Убираем системные отступы шрифта
                            ),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                Row(
                                    modifier = Modifier
                                        .padding(start = 16.dp), // Контролируем отступы сами
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.weight(1f)) {
                                        if (searchText.isEmpty()) {
                                            Text(
                                                text = "Поиск",
                                                fontSize = 18.sp,
                                                color = Color.Gray
                                            )
                                        }
                                        innerTextField() // Сам вводимый текст
                                    }

                                    Image(
                                        painter = painterResource(id = R.drawable.ic_search),
                                        contentDescription = null,
                                        modifier = Modifier.size(42.dp),
                                    )
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Значок уведомлений (40 на 40)
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                Color.White.copy(alpha = 0.2f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_notification),
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clickable {
                                    navController.navigate("notifications")
                                }
                        )
                    }
                }
            }
        }

        // --- СЕТКА ТОВАРОВ ---
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(products) { product ->
                ProductCard(product)
            }
        }
    }
}


@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    MainScreen(navController)
}
