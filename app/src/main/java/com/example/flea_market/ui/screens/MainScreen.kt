package com.example.flea_market.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flea_market.R
import com.example.flea_market.data.Product
import com.example.flea_market.data.ProductCard
import com.example.flea_market.data.network.RetrofitClient
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
import com.example.flea_market.viewmodels.BasketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    basketViewModel: BasketViewModel
    ) {
    // Состояния для поиска
    var searchText by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf("home") }

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Загружаем данные при входе на экран
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getProducts()
            if (response.isSuccessful) {
                products = response.body() ?: emptyList()
            }
        } catch (e: Exception) {
            // Если вообще нет связи с сервером или упал интернет
            android.util.Log.e("API_DEBUG", "Сбой сети: ${e.message}")
            android.widget.Toast.makeText(context, "Проверь подключение к интернету", android.widget.Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
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
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = FleaBlue)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        isInBasket = basketViewModel.basketItems.any { it.id == product.id }, // Проверяем через вьюмодель
                        onBasketClick = { basketViewModel.toggleProduct(product) } // Добавляем/удаляем
                    )
                }
            }
        }
    }
}


@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun MainScreenPreview() {
    val navController = rememberNavController()
    val basketViewModel: BasketViewModel = viewModel()
    MainScreen(navController, basketViewModel)
}
