package com.example.flea_market.ui.screens // Обновленный пакет

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
// Импорты теперь тоже смотрят в com.example
import com.example.flea_market.R
import com.example.flea_market.data.models.AuthResponse
import com.example.flea_market.data.models.LoginRequest
import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.models.User
import com.example.flea_market.ui.theme.BackgroundGray
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
import okhttp3.internal.userAgent

@Composable
fun AuthorisationScreen(
    onNavigateToRegistration: () -> Unit,
    onLoginClick: (AuthResponse) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Состояния ошибок
    var loginError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
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


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Авторизация",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле логина
            AuthTextField(
                value = login,
                onValueChange = { login = it },
                label = "Логин",
                trailingIcon = {
                    if (login.isNotEmpty()) {
                        IconButton(onClick = { login = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Очистить",
                                tint = Color.White
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Поле пароля
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Пароль",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    loginError = if (login.isEmpty()) "Введите логин" else null
                    passwordError = if (password.isEmpty()) "Введите пароль" else null

                    if (loginError == null && passwordError == null) {
                        // 1. Имитируем успешный ответ от сервера
                        // В будущем здесь будет вызов viewModel.login(login, password)
                        val mockResponse = AuthResponse(
                            token = "dummy_token",
                            user = User(
                                login = login,
                                fullName = "Пользователь $login",
                                city = "Омск"
                            )
                        )

                        // 2. ТЕПЕРЬ ТИПЫ СОВПАДАЮТ
                        // Мы передаем AuthResponse, как и просит навигация
                        onLoginClick(mockResponse)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FleaBlue)
            ) {
                Text(
                    text = "Войти",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "У вас еще нет аккаунта? ", color = Color.Black)
                Text(
                    text = "Зарегистрироваться",
                    color = FleaBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToRegistration() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
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
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MarketPink,
            unfocusedContainerColor = MarketPink,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { onPasswordToggle?.invoke() }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            } else {
                trailingIcon?.invoke()
            }
        }
    )
}
@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun AuthorisationScreenPreview() {
    AuthorisationScreen({}, {})
}
