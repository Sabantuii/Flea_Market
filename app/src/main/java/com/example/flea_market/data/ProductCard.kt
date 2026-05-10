package com.example.flea_market.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flea_market.R
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink

@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier
            .size(width = 156.dp, height = 215.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Фото товара (156 на 90, Crop)
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = product.price,
                    color = MarketPink,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = product.desc,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp).weight(3f)
                )
                // Кнопка (высота 26, жирный текст по центру)
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(26.dp)
                        .weight(1f)
                        .padding(bottom = 2.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FleaBlue),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("В КОРЗИНУ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewProductCard() {

    val product = Product(
        1,
        "12.990 ₽",
        "Kingston Fury DDR5 32GB 6000MHz",
        R.drawable.ram
    )
    ProductCard(product)
}