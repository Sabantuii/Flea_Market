package com.example.flea_market.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.flea_market.data.Product

class BasketViewModel : ViewModel() {
    // Список товаров в корзине
    val basketItems = mutableStateListOf<Product>()

    fun toggleProduct(product: Product) {
        android.util.Log.d("BASKET_CHECK", "Жмем на ID: '${product.id}'")
        val exists = basketItems.any { it.id == product.id }
        android.util.Log.d("BASKET_CHECK", "Товар уже есть в корзине? $exists")

        if (exists) {
            basketItems.removeAll { it.id == product.id }
        } else {
            basketItems.add(product)
        }
        android.util.Log.d("BASKET_CHECK", "Текущий размер корзины: ${basketItems.size}")
    }

    fun isProductInBasket(product: Product): Boolean {
        return basketItems.any { it.id == product.id }
    }
    // Удаление из корзины при выходе
    fun clearBasket() {
        basketItems.clear()
        android.util.Log.d("BASKET_CHECK", "Корзина очищена при выходе. Размер: ${basketItems.size}")
    }
}