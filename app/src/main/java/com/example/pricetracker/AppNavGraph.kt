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
            arguments = listOf(navArgument("symbol") { type = NavType.StringType })
        ) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol") ?: return@composable
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
