package com.example.pricetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject

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
    private var feedJob: Job? = null

    private val currentPrices = mutableMapOf<String, Double>()
    private val previousPrices = mutableMapOf<String,Double>()

    init {
        symbols.forEach { symbol ->
            val price = (50..500).random() + Math.random() * 100
            currentPrices[symbol] = price
            previousPrices[symbol] = price
        }
        updateStockList()

        // listen connection state
        viewModelScope.launch {
            webSocketService.connectionState.collect { connected ->
                _uiState.update { it.copy(isNetworkConnected = connected) }
            }
        }

        // listen echo messages
        viewModelScope.launch {
            webSocketService.messages.collect { message ->
                handleEchoMessage(message)
            }
        }
    }

    fun toggleFeed() {
        if (_uiState.value.isFeedActive) {
            stopFeed()
        } else {
            startFeed()
        }
    }

    private fun startFeed() {
        webSocketService.connect()
        _uiState.update { it.copy(isFeedActive = true) }

        feedJob = viewModelScope.launch {
            while(true) {
                delay(2000)
                sendPriceUpdates()
            }
        }
    }

    private fun stopFeed() {
        feedJob?.cancel()
        feedJob = null
        webSocketService.disconnect()
        _uiState.update { it.copy(isFeedActive = false, isNetworkConnected = false) }
    }

    private fun sendPriceUpdates() {
        symbols.forEach { symbol ->
            val currentPrice = currentPrices[symbol] ?: return@forEach
            val newPrice = generateNewPrice(currentPrice)

            val json = JSONObject().apply {
                put("symbol", symbol)
                put("price", newPrice)
            }
            webSocketService.send(json.toString())
        }
    }

    private fun handleEchoMessage(message: String) {
        try {
            val json = JSONObject(message)
            val symbol = json.getString("symbol")
            val newPrice = json.getDouble("price")

            previousPrices[symbol] = currentPrices[symbol] ?: newPrice
            currentPrices[symbol] = newPrice

            updateStockList()
        } catch (e: Exception) {
            // ignore invalid messages
        }
    }

    private fun generateNewPrice(currentPrice: Double): Double {
        val change = currentPrice * (Math.random() * 0.1 - 0.05)
        return (currentPrice + change).coerceAtLeast(1.0)
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

    override fun onCleared() {
        super.onCleared()
        stopFeed()
    }

}
