package com.example.pricetracker

import androidx.compose.ui.graphics.Color

data class StockPrice(
    val symbol: String,
    val price: Double,
    val previousPrice: Double
) {
    val isUp: Boolean get() = price >= previousPrice
    val changeIndicator: String get() = if (isUp) "↑" else "↓"
    val changeColor: Color get() = if (isUp) Color(0xFF4CAF50) else Color(0xFFF44336)
}