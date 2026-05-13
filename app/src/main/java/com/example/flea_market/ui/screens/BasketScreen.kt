package com.example.flea_market.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.flea_market.data.Product
import com.example.flea_market.ui.theme.FleaBlue
import com.example.flea_market.ui.theme.MarketPink
import com.example.flea_market.viewmodels.BasketViewModel

@Composable
fun BasketScreen(
    navController: NavController,
    viewModel: BasketViewModel
) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF2F2F2))) {
        // Шапка корзины
        Box(modifier = Modifier.fillMaxWidth().background(FleaBlue).padding(24.dp).statusBarsPadding()) {
            Text("Корзина", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        if (viewModel.basketItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("В корзине пока пусто :(", color = Color.Gray)
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(viewModel.basketItems) { product ->
                    BasketItemCard(product) { viewModel.toggleProduct(product) }
                }
            }
        }
    }
}
@Composable
fun BasketItemCard(product: Product, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
            // Маленькое фото
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                Text(product.name, fontSize = 14.sp, maxLines = 1, fontWeight = FontWeight.Medium)
                Text("${product.price} ₽", color = MarketPink, fontWeight = FontWeight.Bold)
            }

            // Иконка ведра
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить", tint = Color.Gray)
            }
        }
    }
}
