package com.example.flea_market.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flea_market.R
import com.example.flea_market.ui.theme.BackgroundGray
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onBackToLogin: () -> Unit,
    onRegisterClick: () -> Unit
) {
    // Состояния для полей ввода
    var phone by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    // Состояния для видимости пароля
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState()) // Добавляем скролл, так как полей много
    ) {
        // --- ШАПКА ---
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_welcome_header),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // --- КОНТЕНТ ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Регистрация",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Поля ввода (используем розовый цвет MarketPink для полей, как на макете)
            CustomRegistrationField(value = phone, onValueChange = { phone = it }, label = "Телефон")
            Spacer(modifier = Modifier.height(12.dp))

            CustomRegistrationField(value = nickname, onValueChange = { nickname = it }, label = "Логин")
            Spacer(modifier = Modifier.height(12.dp))

            CustomRegistrationField(value = email, onValueChange = { email = it }, label = "Почта")
            Spacer(modifier = Modifier.height(12.dp))

            // Поле Адреса (пока текстовое, но с иконкой "карты" для будущего)
            CustomRegistrationField(
                value = address,
                onValueChange = { address = it },
                label = "Адрес",
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
                        contentDescription = "Выбрать на карте",
                        tint = Color.White,
                        modifier = Modifier.clickable { /* Тут будет вызов карты */ }
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Пароль
            CustomRegistrationField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible }
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Повтор пароля
            CustomRegistrationField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = "Повторите пароль",
                isPassword = true,
                isPasswordVisible = repeatPasswordVisible,
                onPasswordToggle = { repeatPasswordVisible = !repeatPasswordVisible }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка Регистрация
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FleaBlue)
            ) {
                Text(text = "Регистрация", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Текст внизу
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Уже есть аккаунт? ", color = Color.Black)
                Text(
                    text = "Авторизоваться",
                    color = FleaBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onBackToLogin() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomRegistrationField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggle: (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        label = {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp // Делаем подсказку чуть меньше, когда она сверху
            )
        },

        // 2. НАСТРОЙКА ВВОДИМОГО ТЕКСТА
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 20.sp, // Увеличиваем шрифт вводимых слов/цифр
            fontWeight = FontWeight.Medium,
            color = Color.White
        ),

        colors = TextFieldDefaults.colors( // <--- Просто .colors вместо .textFieldColors
            focusedContainerColor = MarketPink,
            unfocusedContainerColor = MarketPink,
            disabledContainerColor = MarketPink,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { onPasswordToggle?.invoke() }) {
                    Icon(
                        // Используем стандартные иконки из библиотеки Material Icons
                        imageVector = if (isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Скрыть пароль" else "Показать пароль",
                        tint = Color.White
                    )
                }
            }
        }
    )
}
@Preview(showBackground = true, device = "spec:width=411dp,height=1100dp")
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen({}, {})
}