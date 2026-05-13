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
import androidx.compose.foundation.text.KeyboardOptions
import com.example.flea_market.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flea_market.UserSession
import com.example.flea_market.data.network.RetrofitClient
import com.example.flea_market.data.repository.AuthRepository
import com.example.flea_market.utils.AuthValidator
import com.example.flea_market.utils.PhoneVisualTransformation
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // Достаем репозиторий и скоуп для запросов
    val repository = remember { AuthRepository(RetrofitClient.instance) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // 1. ИНИЦИАЛИЗАЦИЯ: Берем данные из сохраненной сессии
    val user = UserSession.currentUser

    var login by remember { mutableStateOf(user?.login ?: "") }
    var fullName by remember { mutableStateOf(user?.fullName ?: "") }
    var phone by remember { mutableStateOf(user?.phone ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "") }

    // РАЗДЕЛЯЕМ АДРЕС
    var city by remember { mutableStateOf(user?.city ?: "") }
    var street by remember { mutableStateOf(user?.street ?: "") }
    var house by remember { mutableStateOf(user?.house ?: "") }
    var apartment by remember { mutableStateOf(user?.apartment ?: "") }

    // Состояния ошибок
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var loginError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Бэкапы для отмены
    var loginBackup by remember { mutableStateOf("") }
    var nameBackup by remember { mutableStateOf("") }
    var phoneBackup by remember { mutableStateOf("") }
    var emailBackup by remember { mutableStateOf("") }
    var passwordBackup by remember { mutableStateOf("") }

    // РАЗДЕЛЯЕМ АДРЕС
    var cityBackup by remember { mutableStateOf(user?.city ?: "") }
    var streetBackup by remember { mutableStateOf(user?.street ?: "") }
    var houseBackup by remember { mutableStateOf(user?.house ?: "") }
    var apartmentBackup by remember { mutableStateOf(user?.apartment ?: "") }
    var isEditing by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Следим за объектом user. Как только он изменится (придет из API),
// мы обновим локальные переменные экрана.
    LaunchedEffect(user) {
        user?.let {
            login = it.login
            password = it.password ?: ""
            fullName = it.fullName ?: ""
            val rawPhone = it.phone ?: ""
            // Оставляем только цифры и берем только последние 10
            phone = rawPhone.filter { char -> char.isDigit() }.takeLast(10)
            email = it.email ?: ""
            city = it.city ?: ""
            street = it.street ?: ""
            house = it.house ?: ""
            apartment = it.apartment ?: ""
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
        ) {
            // ШАПКА
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(MarketPink)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text("Профиль", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ФОТО
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.default_avatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.LightGray, CircleShape)
                )
            }

            // РЕДАКТИРОВАТЬ / ОТМЕНИТЬ
            TextButton(
                onClick = {
                    if (!isEditing) {
                        // НАЧАЛО РЕДАКТИРОВАНИЯ: сохраняем всё в бэкап
                        loginBackup = login
                        nameBackup = fullName
                        phoneBackup = phone
                        emailBackup = email
                        passwordBackup = password

                        // Бэкап адреса по частям
                        cityBackup = city
                        streetBackup = street
                        houseBackup = house
                        apartmentBackup = apartment

                        isEditing = true
                    } else {
                        // ОТМЕНА: возвращаем всё из бэкапа
                        login = loginBackup
                        fullName = nameBackup
                        phone = phoneBackup
                        email = emailBackup
                        password = passwordBackup

                        // Откат адреса
                        city = cityBackup
                        street = streetBackup
                        house = houseBackup
                        apartment = apartmentBackup

                        // Сбрасываем ошибки, чтобы при следующем входе всё было чисто
                        loginError = null
                        phoneError = null
                        emailError = null
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

            // ПОЛЯ
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                ProfileField(
                    label = "Логин",
                    value = login,
                    onValueChange = { login = it; loginError = null },
                    enabled = isEditing,
                    isError = loginError != null,
                    errorMessage = loginError
                )

                ProfileField(
                    label = "Ваше ФИО",
                    value = fullName,
                    onValueChange = { fullName = it },
                    enabled = isEditing
                )
                // ТЕЛЕФОН
                ProfileField(
                    label = "Телефон",
                    value = phone,
                    onValueChange = { newValue ->
                        // Оставляем только цифры и ограничиваем длину (10 цифр без 7/8)
                        val digitsOnly = newValue.filter { it.isDigit() }
                        if (digitsOnly.length <= 10) {
                            phone = digitsOnly
                            phoneError = null
                        }
                    },
                    enabled = isEditing,
                    isError = phoneError != null,
                    errorMessage = phoneError,
                    visualTransformation = PhoneVisualTransformation(), // МАСКА
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                // АДРЕС
                Text("Адрес", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MarketPink)

                ProfileField(
                    label = "Город",
                    value = city,
                    onValueChange = { city = it },
                    enabled = isEditing
                )

                ProfileField(
                    label = "Улица",
                    value = street,
                    onValueChange = { street = it },
                    enabled = isEditing
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        ProfileField(
                            label = "Дом",
                            value = house,
                            onValueChange = { house = it },
                            enabled = isEditing
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        ProfileField(
                            label = "Кв.",
                            value = apartment,
                            onValueChange = { apartment = it },
                            enabled = isEditing
                        )
                    }
                }
                ProfileField(
                    label = "Почта",
                    value = email,
                    onValueChange = { email = it; emailError = null },
                    enabled = isEditing,
                    isError = emailError != null,
                    errorMessage = emailError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                // ПАРОЛЬ
                Text("Пароль", fontWeight = FontWeight.Bold, color = FleaBlue, fontSize = 18.sp)
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    enabled = isEditing,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                null
                            )
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // КНОПКА СОХРАНИТЬ
                if (isEditing) {
                    Button(
                        onClick = {
                            // Подготовка номера добавляем 7, если её нет, и убираем лишнее
                            val cleanPhone = phone.filter { it.isDigit() }
                            val formattedPhone = when {
                                cleanPhone.startsWith("8") -> "7" + cleanPhone.substring(1)
                                cleanPhone.length == 10 -> "7" + cleanPhone
                                else -> cleanPhone
                            }

                            // Валидация
                            loginError = if (login.length < 3) "Логин слишком короткий" else null
                            phoneError = AuthValidator.validatePhone(phone)
                            emailError = AuthValidator.validateEmail(email)
                            passwordError = AuthValidator.validatePassword(password)

                            if (loginError == null && phoneError == null &&
                                emailError == null && passwordError == null) {

                                scope.launch {
                                    isEditing = false
                                    snackbarHostState.showSnackbar("Данные обновлены")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = FleaBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Сохранить", color = Color.White)
                    }
                }

                // КНОПКА ВЫХОД
                OutlinedButton(
                    onClick = {
                        UserSession.currentUser = null
                        navController.navigate("authorisation") { popUpTo(0) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Выйти из аккаунта", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FleaBlue)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                if (isError && errorMessage != null) {
                    Text(text = errorMessage, color = Color.Red)
                }
            },
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color(0xFFF2F2F2),
                focusedContainerColor = Color(0xFFE8E8E8),
                unfocusedContainerColor = Color(0xFFF2F2F2),
                disabledTextColor = Color.Black,
                errorContainerColor = Color(0xFFFFEBEE) // Светло-красный фон при ошибке
            ),
            shape = RoundedCornerShape(8.dp),
            textStyle = TextStyle(fontSize = 18.sp)
        )
    }
}

@Preview(device = "spec:width=411dp,height=1410dp")
@Composable
fun Show() {
    val navController = rememberNavController()
    ProfileScreen(navController)
}