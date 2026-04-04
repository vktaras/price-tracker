package com.example.pricetracker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class StockPrice(
    val symbol: String,
    val price: Double,
    val previousPrice: Double
) {
    val isUp: Boolean get() = price >= previousPrice
    val changeIndicator: String get() = if (isUp) "↑" else "↓"
    val changeColor: Color get() = if (isUp) Color(0xFF4CAF50) else Color(0xFFF44336)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen() {
    val stocks = listOf(
        StockPrice("NVDA", 950.42, 940.10),
        StockPrice("AAPL", 187.68, 189.20),
        StockPrice("GOOG", 176.35, 174.80),
        StockPrice("TSLA", 172.50, 175.30),
        StockPrice("AMZN", 185.60, 183.90),
        StockPrice("MSFT", 420.15, 418.70),
    ).sortedByDescending { it.price }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Tracker") },
                navigationIcon = {
                    Text(
                        text = "🟢",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                actions = {
                    Text(
                        text = "Stop",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { }
                    )
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(stocks) { stock ->
                StockRow(stock = stock, onClick = { })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun StockRow(stock: StockPrice, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stock.symbol,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$%.2f".format(stock.price),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = stock.changeIndicator,
                color = stock.changeColor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
