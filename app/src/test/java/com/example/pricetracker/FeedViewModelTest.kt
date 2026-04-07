package com.example.pricetracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // initial
    @Test
    fun `initial state has 25 stocks`() {
        val viewModel = FeedViewModel()
        val state = viewModel.uiState.value

        assertEquals(25, state.stocks.size)
    }

    @Test
    fun `initial state feed is not active`() {
        val viewModel = FeedViewModel()

        assertFalse(viewModel.uiState.value.isFeedActive)
    }

    @Test
    fun `initial state is not connected`() {
        val viewModel = FeedViewModel()

        assertFalse(viewModel.uiState.value.isNetworkConnected)
    }


    // sorting
    @Test
    fun `stocks are sorted by price descending`() {
        val viewModel = FeedViewModel()
        val prices = viewModel.uiState.value.stocks.map { it.price }

        assertEquals(prices, prices.sortedDescending())
    }

    @Test
    fun `all stock symbols are unique`() {
        val viewModel = FeedViewModel()
        val symbols = viewModel.uiState.value.stocks.map { it.symbol }

        assertEquals(symbols.size, symbols.distinct().size)
    }

    @Test
    fun `all prices are positive`() {
        val viewModel = FeedViewModel()

        viewModel.uiState.value.stocks.forEach { stock ->
            assertTrue("${stock.symbol} price should be positive", stock.price > 0)
        }
    }

    @Test
    fun `stock price change indicator is correct`() {
        val stockUp = StockPrice("TEST", 100.0, 90.0)
        val stockDown = StockPrice("TEST", 80.0, 90.0)

        assertTrue(stockUp.isUp)
        assertEquals("↑", stockUp.changeIndicator)

        assertFalse(stockDown.isUp)
        assertEquals("↓", stockDown.changeIndicator)
    }
}
