package com.example.pricetracker

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedViewModel = viewModel(),
    onStockClick: (StockPrice) -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Price Tracker") },
                navigationIcon = {
                    Text(
                        text = if (uiState.isNetworkConnected) "🟢" else "🔴",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                actions = {
                    Text(
                        text = if (uiState.isFeedActive) "Stop" else "Start",
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable { viewModel.toggleFeed() }
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
            items(uiState.stocks, key = { it.symbol }) { stock ->
                StockRow(stock = stock, onClick = { onStockClick(stock) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun StockRow(stock: StockPrice, onClick: () -> Unit) {

    var isFlashing by remember { mutableStateOf(false) }
    var flashColor by remember { mutableStateOf(Color.Transparent) }

    LaunchedEffect(stock.price) {
        if (stock.price != stock.previousPrice) {
            flashColor = if (stock.isUp) Color(0x334CAF50) else Color(0x33F44336)
            isFlashing = true
            delay(1000)
            isFlashing = false
        }
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isFlashing) flashColor else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "flash"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
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
