package com.example.flea_market.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flea_market.R
import com.example.flea_market.ui.theme.BackgroundGray
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
// новое чето для индикатора загрузки
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.StrokeCap
import kotlinx.coroutines.delay


@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {

    // --- НОВОЕ: СОСТОЯНИЯ ---
    var isLoading by remember { mutableStateOf(false) }

    // Анимация числа от 0 до 1 за 3.5 секунды
    val progress by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(durationMillis = 3500),
        label = "ProgressAnimation"
    )

    // Эффект задержки перед переходом

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(3500) // Ждем завершения анимации
            onStartClick() // Уходим на следующий экран
        }
    }

    // -----------------------

    // Вся композиция - это Колонка, чтобы элементы были друг под другом
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray) // Фон нижней части
            .verticalScroll(rememberScrollState())
    ) {
        // --- ШАПКА ---
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_welcome_header),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // --- 2. ТЕКСТОВЫЙ БЛОК ---
        // Ты написал: отступ от шапки 36dp, по бокам по 16dp.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Это заставляет колонку занять все свободное место
                .padding(horizontal = 16.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Текст "Добро пожаловать" (размер 40sp)
            Text(
                text = "Добро\nпожаловать",
                fontSize = 40.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                lineHeight = 44.sp // Немного добавил высоты строки для читаемости
            )

            // Отступ между фразами (я добавил небольшой, т.к. в ТЗ нет, но на глаз 16dp)
            Spacer(modifier = Modifier.height(16.dp))

            // Фраза "Flea Market" (размер 52sp)
            // Используем AnnotatedString, чтобы покрасить слова в разные цвета
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = FleaBlue)) { // Синий
                        append("Flea ")
                    }
                    withStyle(style = SpanStyle(color = MarketPink)) { // Розовый
                        append("Market")
                    }
                },
                fontSize = 52.sp,
                fontWeight = FontWeight.ExtraBold, // Пожирнее
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // --- НОВОЕ: ИНДИКАТОР ЗАГРУЗКИ ---
        // Показываем его только в режиме загрузки
        if (isLoading) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(8.dp),
                color = MarketPink, // Твой розовый
                trackColor = FleaBlue.copy(alpha = 0.2f), // Бледный синий фон
                strokeCap = StrokeCap.Round // Красивые круглые края
            )
        }

        // --- КНОПКА "НАЧАТЬ" ---
        Button(
            onClick = { if (!isLoading) isLoading = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp) // Чуть увеличил высоту из-за падингов, чтобы сама кнопка была 56dp
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLoading) FleaBlue.copy(alpha = 0.7f) else FleaBlue,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(0.dp) // Обнуляем, чтобы стрелка была вплотную к краю
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // НОВОЕ: Текст меняется при загрузке
                Text(
                    text = if (isLoading) "Загрузка..." else "Начать",
                    fontSize = if (isLoading) 28.sp else 42.sp, // Немного уменьшил для слова "Загрузка"
                    fontWeight = FontWeight.Bold
                )
                // НОВОЕ: Скрываем стрелку во время загрузки
                if (!isLoading) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }

        // Отступ от нижней границы экрана 16dp
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// --- ПРЕВЬЮ ДЛЯ ANDROID STUDIO ---
@Preview(
    showBackground = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onStartClick = {})
}