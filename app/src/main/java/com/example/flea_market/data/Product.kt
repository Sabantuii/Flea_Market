package com.example.flea_market.data

data class Product(
    val id: String, // В БД у тебя GUID (строка)
    val name: String,
    val price: Double, // В БД Price - это decimal/float
    val imageUrl: String
)
