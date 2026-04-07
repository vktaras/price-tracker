package com.example.pricetracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val feedViewModel: FeedViewModel = viewModel()


    NavHost(navController = navController, startDestination = "feed") {

        composable("feed") {
            FeedScreen(
                viewModel = feedViewModel,
                onStockClick = { stock ->
                    navController.navigate("details/${stock.symbol}")
                }
            )
        }

        composable(
            route = "details/{symbol}",
            arguments = listOf(navArgument("symbol") { type = NavType.StringType }),
            deepLinks = listOf(navDeepLink { uriPattern = "stocks://symbol/{symbol}" })
        ) {
            val detailsViewModel: DetailsViewModel = viewModel()
            val symbol = detailsViewModel.symbol

            val uiState by feedViewModel.uiState.collectAsStateWithLifecycle()
            val stock = uiState.stocks.find { it.symbol == symbol }

            DetailsScreen(
                symbol = symbol,
                currentPrice = stock?.price ?: 0.0,
                previousPrice = stock?.previousPrice ?: 0.0,
                onBack = { navController.popBackStack() }
            )
        }


    }
}
