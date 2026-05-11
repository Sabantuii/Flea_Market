package com.example.flea_market

import com.example.flea_market.data.models.RegisterRequest
import com.example.flea_market.data.models.User

//object UserSession {
//    var currentUser: RegisterRequest? = null
//}
object UserSession {
    var currentUser: User? = null // Теперь тут хранится универсальный объект User
}