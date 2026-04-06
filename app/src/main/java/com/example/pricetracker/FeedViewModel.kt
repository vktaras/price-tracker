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

private val symbols = listOf(
    "AAPL", "GOOG", "TSLA", "AMZN", "MSFT",
    "NVDA", "META", "NFLX", "BABA", "V",
    "JPM", "WMT", "PG", "MA", "DIS",
    "PYPL", "INTC", "AMD", "CSCO", "ORCL",
    "CRM", "ADBE", "QCOM", "TXN", "AVGO"
)

class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    private val webSocketService = PriceWebSocketService()

    private val currentPrices = mutableMapOf<String, Double>()
    private val previousPrices = mutableMapOf<String,Double>()

    init {
        symbols.forEach { symbol ->
            val price = (50..500).random() + Math.random() * 100
            currentPrices[symbol] = price
            previousPrices[symbol] = price
        }
        updateStockList()
    }

    private fun updateStockList() {
        val stocks = symbols.map { symbol ->
            StockPrice(
                symbol = symbol,
                price = currentPrices[symbol] ?: 0.0,
                previousPrice = previousPrices[symbol] ?: 0.0
            )
        }.sortedByDescending { it.price }

        _uiState.update { it.copy(stocks = stocks) }
    }

}
