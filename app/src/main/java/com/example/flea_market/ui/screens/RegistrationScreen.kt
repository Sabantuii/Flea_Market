package com.example.flea_market.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flea_market.R
import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.ui.theme.BackgroundGray
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
import com.example.flea_market.utils.AuthValidator
import com.example.flea_market.utils.PhoneVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    onBackToLogin: () -> Unit,
    onRegisterClick: (RegisterRequest) -> Unit
) {
    // Состояния для полей ввода
    var phone by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }

    // РАЗДЕЛЕННЫЙ АДРЕС (как в профиле)
    var city by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var house by remember { mutableStateOf("") }
    var apartment by remember { mutableStateOf("") }

    // Состояния ошибок
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Состояния для видимости пароля
    var passwordVisible by remember { mutableStateOf(false) }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
            .verticalScroll(rememberScrollState())
    ) {
        // --- ШАПКА (без изменений) ---
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.bg_welcome_header),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
            Text(text = "Регистрация", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(24.dp))

            // 1. ТЕЛЕФОН С МАСКОЙ
            CustomRegistrationField(
                value = phone,
//                onValueChange = { if (it.length <= 10) phone = it; phoneError = null },
                onValueChange = { newValue ->
                    // Фильтруем: оставляем только цифры и не более 10 штук
                    val digitsOnly = newValue.filter { it.isDigit() }
                    if (digitsOnly.length <= 10) {
                        phone = digitsOnly
                        phoneError = null
                    }
                },
                label = "Телефон",
                isError = phoneError != null,
                errorMessage = phoneError,
                visualTransformation = PhoneVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomRegistrationField(value = nickname, onValueChange = { nickname = it }, label = "Логин")
            Spacer(modifier = Modifier.height(12.dp))

            CustomRegistrationField(value = fullname, onValueChange = { fullname = it }, label = "ФИО")
            Spacer(modifier = Modifier.height(12.dp))

            // 2. ПОЧТА С ВАЛИДАЦИЕЙ
            CustomRegistrationField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = "Почта",
                isError = emailError != null,
                errorMessage = emailError
            )

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Адрес доставки", fontWeight = FontWeight.Bold, color = FleaBlue)
            Spacer(modifier = Modifier.height(8.dp))

            // 3. БЛОК АДРЕСА
            CustomRegistrationField(value = city, onValueChange = { city = it }, label = "Город")
            Spacer(modifier = Modifier.height(12.dp))
            CustomRegistrationField(value = street, onValueChange = { street = it }, label = "Улица")
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    CustomRegistrationField(value = house, onValueChange = { house = it }, label = "Дом")
                }
                Box(modifier = Modifier.weight(1f)) {
                    CustomRegistrationField(value = apartment, onValueChange = { apartment = it }, label = "Кв.")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. ПАРОЛИ
            CustomRegistrationField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = "Пароль",
                isPassword = true,
                isPasswordVisible = passwordVisible,
                onPasswordToggle = { passwordVisible = !passwordVisible }
            )
            Spacer(modifier = Modifier.height(12.dp))

            CustomRegistrationField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it; passwordError = null },
                label = "Повторите пароль",
                isPassword = true,
                isError = passwordError != null,
                errorMessage = passwordError,
                isPasswordVisible = repeatPasswordVisible,
                onPasswordToggle = { repeatPasswordVisible = !repeatPasswordVisible }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка Регистрация
            Button(
                onClick = {
                    // ПРОВЕРКА ПЕРЕД ОТПРАВКОЙ
                    phoneError = AuthValidator.validatePhone(phone)
                    emailError = AuthValidator.validateEmail(email)
                    passwordError = AuthValidator.validatePasswordsMatch(password, repeatPassword)

                    if (phoneError == null && emailError == null && passwordError == null) {
                        val request = RegisterRequest(
                            login = nickname,
                            password = password,
                            fullName = fullname,
                            email = email,
                            phone = phone,
                            city = city,
                            street = street,
                            house = house,
                            apartment = apartment
                        )
                        onRegisterClick(request)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = FleaBlue)
            ) {
                Text(text = "Регистрация", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))

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
    // НОВЫЕ ПАРАМЕТРЫ ДЛЯ ВАЛИДАЦИИ И МАСОК:
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError, // Включает красную рамку, если true
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp), // Чуть увеличил высоту, чтобы текст не прилипал
            label = {
                Text(
                    text = label,
                    color = if (isError) Color.Red else Color.White.copy(alpha = 0.7f),
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
                disabledContainerColor = MarketPink,
                errorContainerColor = MarketPink, // Чтобы при ошибке фон оставался розовым
                focusedIndicatorColor = if (isError) Color.Red else Color.Transparent,
                unfocusedIndicatorColor = if (isError) Color.Red else Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                errorTextColor = Color.White,
                focusedLabelColor = Color.White,
                unfocusedLabelColor = Color.White,
                errorLabelColor = Color.Red
            ),
            shape = RoundedCornerShape(8.dp),
            // Теперь трансформация передается снаружи (для пароля или маски телефона)
            visualTransformation = if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                visualTransformation
            },
            keyboardOptions = keyboardOptions,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { onPasswordToggle?.invoke() }) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                } else {
                    trailingIcon?.invoke()
                }
            }
        )

        // ВЫВОД ТЕКСТА ОШИБКИ ПОД ПОЛЕМ
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }
    }
}
@Preview(showBackground = true, device = "spec:width=411dp,height=1100dp")
@Composable
fun RegistrationScreenPreview() {
    RegistrationScreen({}, {})
}