package com.example.pricetracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class FeedUiState (
    val stocks: List<StockPrice> = emptyList(),
    val isFeedActive: Boolean = false,
    val isNetworkConnected: Boolean = false
)

class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        val initialStocks = listOf(
            StockPrice("NVDA", 950.42, 940.10),
            StockPrice("AAPL", 187.68, 189.20),
            StockPrice("GOOG", 176.35, 174.80),
            StockPrice("TSLA", 172.50, 175.30),
            StockPrice("AMZN", 185.60, 183.90),
            StockPrice("MSFT", 420.15, 418.70)
        ).sortedByDescending { it.price }

        _uiState.update { it.copy(stocks = initialStocks) }
    }

}