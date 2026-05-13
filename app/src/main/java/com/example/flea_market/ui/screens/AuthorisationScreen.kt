package com.example.flea_market.ui.screens

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
import com.example.flea_market.data.network.RetrofitClient
import com.example.flea_market.ui.theme.BackgroundGray
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent

@Composable
fun AuthorisationScreen(
    onNavigateToRegistration: () -> Unit,
    onLoginClick: (AuthResponse) -> Unit
) {
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Состояния для визуальной подсветки ошибок
    var isLoginError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // --- ШАПКА ---
        Box(
            modifier = Modifier.fillMaxWidth(),
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
                onValueChange = {
                    login = it
                    isLoginError = false // Убираем красноту при начале ввода
                    errorMessage = null
                },
                label = "Логин",
                isError = isLoginError,
                trailingIcon = {
                    if (login.isNotEmpty()) {
                        IconButton(onClick = { login = "" }) {
                            Icon(Icons.Default.Close, "Очистить", tint = Color.White)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Поле пароля
            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordError = false // Убираем красноту при начале ввода
                    errorMessage = null
                },
                label = "Пароль",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible },
                isError = isPasswordError
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Вывод текста ошибки
            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Medium
                )
            }

            Button(
                onClick = {
                    // 1. Проверка на пустые поля перед запросом
                    val loginEmpty = login.isBlank()
                    val passwordEmpty = password.isBlank()

                    isLoginError = loginEmpty
                    isPasswordError = passwordEmpty

                    if (loginEmpty || passwordEmpty) {
                        errorMessage = "Заполните все поля"
                        return@Button
                    }

                    // 2. Если поля не пустые, идем в БД
                    scope.launch {
                        isLoading = true
                        try {
                            val response = RetrofitClient.instance.login(LoginRequest(login, password))

                            if (response.isSuccessful && response.body() != null) {
                                onLoginClick(response.body()!!)
                            } else {
                                errorMessage = "Неверный логин или пароль"
                                isLoginError = true
                                isPasswordError = true
                            }
                        } catch (e: Exception) {
                            errorMessage = "Ошибка сети. Проверь сервер!"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading, // Блокируем кнопку при загрузке
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FleaBlue)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Войти",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
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
    isError: Boolean = false, // Добавили параметр
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError, // Передаем состояние ошибки в TextField
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        label = {
            Text(
                text = label,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        },
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MarketPink,
            unfocusedContainerColor = MarketPink,
            errorContainerColor = MarketPink, // Розовый даже при ошибке
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Red, // Но индикатор будет красным
            cursorColor = Color.White,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            errorTextColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            errorLabelColor = Color.Red
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
