package com.example.flea_market.ui.screens

import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import com.example.flea_market.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // 1. Создаем структуру данных нашего профиля
    // Используем Map или просто отдельные переменные, но для отката удобнее так:
    var name by remember { mutableStateOf("Иванов Иван Иваныч") }
    var address by remember { mutableStateOf("Пушкина 42") }
    var phone by remember { mutableStateOf("+7(900) 954-54-54") }
    var email by remember { mutableStateOf("Ivan1994@mail.ru") }
    var password by remember { mutableStateOf("password123") }

    // 2. Переменные для "снимка" данных (бэкап)
    var nameBackup by remember { mutableStateOf("") }
    var addressBackup by remember { mutableStateOf("") }
    var phoneBackup by remember { mutableStateOf("") }
    var emailBackup by remember { mutableStateOf("") }
    var passwordBackup by remember { mutableStateOf("") }

    // Состояния интерфейса
    var isEditing by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()) // Чтобы экран можно было скроллить
    ) {
        // 1. РОЗОВАЯ ШАПКА
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(MarketPink)
                .statusBarsPadding() // Чтобы заходило под статус-бар красиво
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Профиль",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. ФОТО ПРОФИЛЯ
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 90.dp), // Отступы по бокам 90 по ТЗ
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.default_avatar), // Твоя картинка Тони Старка
                contentDescription = "Аватар",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, CircleShape)
                    .clickable { /* Логика смены фото */ }
            )
        }

        // 3. НАДПИСЬ РЕДАКТИРОВАТЬ
        TextButton(
            onClick = {
                if (!isEditing) {
                    // НАЧАЛО РЕДАКТИРОВАНИЯ: сохраняем всё в бэкап
                    nameBackup = name
                    addressBackup = address
                    phoneBackup = phone
                    emailBackup = email
                    passwordBackup = password
                    isEditing = true
                } else {
                    // ОТМЕНА: возвращаем всё из бэкапа
                    name = nameBackup
                    address = addressBackup
                    phone = phoneBackup
                    email = emailBackup
                    password = passwordBackup
                    isEditing = false
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = if (isEditing) "Отменить" else "Редактировать профиль",
                color = Color.Blue,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 4. ПОЛЯ ДАННЫХ
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileField(
                label = "Ваше имя",
                value = name,
                onValueChange = { name = it },
                enabled = isEditing
            )
            ProfileField(
                label = "Адрес",
                value = address,
                onValueChange = { address = it },
                enabled = isEditing
            )
            ProfileField(
                label = "Телефон",
                value = phone,
                onValueChange = { phone = it },
                enabled = isEditing
            )
            ProfileField(
                label = "Почта",
                value = email,
                onValueChange = { email = it },
                enabled = isEditing
            )

            // Поле пароля с глазиком
            Text(
                text = "Пароль",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = FleaBlue
            )
            TextField(
                value = password,
                onValueChange = { password = it },
                enabled = isEditing,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color(0xFFF2F2F2),
                    focusedContainerColor = Color(0xFFE8E8E8),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    disabledTextColor = Color.Black,
                    focusedIndicatorColor = FleaBlue,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 5. КНОПКА СОХРАНИТЬ (появляется только при редактировании)
        if (isEditing) {
            Button(
                onClick = {
                    isEditing = false
                    // Здесь будет вызов сохранения в базу данных
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FleaBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Сохранить", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

}


@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FleaBlue)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color(0xFFF2F2F2),
                focusedContainerColor = Color(0xFFE8E8E8),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledTextColor = Color.Black, // Текст остается черным, даже если выключен
                focusedIndicatorColor = FleaBlue,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(fontSize = 18.sp)
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp")
@Composable
fun Show() {
    val navController = rememberNavController()
    ProfileScreen(navController)
}