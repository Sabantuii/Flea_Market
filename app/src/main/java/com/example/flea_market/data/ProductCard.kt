package com.example.flea_market.data

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import coil.compose.AsyncImage
import com.example.flea_market.R
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink

@Composable
fun ProductCard(
    product: Product,
    isInBasket: Boolean, // Передаем состояние
    onBasketClick: () -> Unit // Действие при нажатии
) {
    val buttonColor = if (isInBasket) Color(0xFF8BB5FF) else FleaBlue
    val buttonText = if (isInBasket) "В КОРЗИНЕ" else "В КОРЗИНУ"

    Card(
        modifier = Modifier.fillMaxWidth().height(230.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth().height(110.dp)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text("${product.price} ₽", color = MarketPink, fontWeight = FontWeight.Bold)
                Text(product.name, fontSize = 12.sp, maxLines = 2, modifier = Modifier.weight(1f))

                Button(
                    onClick = { onBasketClick() },
                    modifier = Modifier.fillMaxWidth().height(32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(buttonText, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewProductCard() {

    ProductCard(Product(
        "1",
        "Intel",
        1999.00,
        "https://ya.ru/images/search?text=кулер&pos=1&rpt=simage&img_url=https%3A%2F%2Favatars.mds.yandex.net%2Fget-mpic%2F12018251%2F2a000001993d179ea6d0230473f14f52917d%2Forig&from=tabbar&lr=66"
    ), isInBasket = false, {})
}