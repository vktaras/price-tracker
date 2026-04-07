package com.example.pricetracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    symbol: String,
    currentPrice: Double,
    previousPrice: Double,
    onBack: () -> Unit
) {
    val stock = StockPrice(symbol, currentPrice, previousPrice)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(symbol) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stock.symbol,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "$%.2f".format(stock.price),
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = stock.changeIndicator,
                color = stock.changeColor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = getStockDescription(symbol),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

private fun getStockDescription(symbol: String): String {
    return when(symbol) {
        "AAPL" -> "Apple Inc. designs, manufactures, and markets smartphones, personal computers, tablets, wearables, and accessories worldwide."
        "GOOG" -> "Alphabet Inc. offers various products and platforms in the United States, Europe, the Middle East, Africa, the Asia-Pacific, Canada, and Latin America."
        "TSLA" -> "Tesla, Inc. designs, develops, manufactures, leases, and sells electric vehicles, and energy generation and storage systems."
        "AMZN" -> "Amazon.com, Inc. engages in the retail sale of consumer products, advertising, and subscription services."
        "MSFT" -> "Microsoft Corporation develops and supports software, services, devices, and solutions worldwide."
        "NVDA" -> "NVIDIA Corporation provides graphics and compute and networking solutions in the United States, Taiwan, China, Hong Kong, and internationally."
        "META" -> "Meta Platforms, Inc. engages in the development of products that enable people to connect and share through mobile devices, PCs, virtual reality headsets, and wearables."
        "NFLX" -> "Netflix, Inc. provides entertainment services. It offers TV series, documentaries, feature films, and games across various genres and languages."
        "BABA" -> "Alibaba Group Holding Limited, through its subsidiaries, provides technology infrastructure and marketing reach to help merchants, brands, retailers, and other businesses."
        "V" -> "Visa Inc. operates as a payments technology company worldwide."
        "JPM" -> "JPMorgan Chase & Co. operates as a financial services company worldwide."
        "WMT" -> "Walmart Inc. engages in the operation of retail, wholesale, and other units worldwide."
        "PG" -> "The Procter & Gamble Company provides branded consumer packaged goods worldwide."
        "MA" -> "Mastercard Incorporated, a technology company, provides transaction processing and other payment-related products and services."
        "DIS" -> "The Walt Disney Company operates as an entertainment company worldwide."
        "PYPL" -> "PayPal Holdings, Inc. operates a technology platform for digital payments on behalf of merchants and consumers worldwide."
        "INTC" -> "Intel Corporation designs, develops, manufactures, markets, and sells computing and related products and services worldwide."
        "AMD" -> "Advanced Micro Devices, Inc. operates as a semiconductor company worldwide."
        "CSCO" -> "Cisco Systems, Inc. designs, manufactures, and sells Internet Protocol based networking and other products."
        "ORCL" -> "Oracle Corporation offers products and services that address enterprise information technology environments worldwide."
        "CRM" -> "Salesforce, Inc. provides Customer Relationship Management technology that brings companies and customers together worldwide."
        "ADBE" -> "Adobe Inc. operates as a diversified software company worldwide."
        "QCOM" -> "QUALCOMM Incorporated engages in the development and commercialization of foundational technologies for the wireless industry worldwide."
        "TXN" -> "Texas Instruments Incorporated designs, manufactures, and sells semiconductors to electronics designers and manufacturers worldwide."
        "AVGO" -> "Broadcom Inc. designs, develops, and supplies various semiconductor and infrastructure software solutions."
        else -> "Stock information not available."
    }
}
