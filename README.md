# Price Tracker

Real-time stock price tracker Android app built with Jetpack Compose.

## Features

- Live price tracking for 25 stock symbols via WebSocket
- Real-time price updates every 2 seconds
- Price change indicators with color coding
- Price flash animation on price change
- Feed sorted by price, highest first
- Connection status indicator
- Start/Stop feed control
- Symbol details screen with description
- Deep link support (stocks://symbol/{symbol})

## Architecture

MVVM with unidirectional data flow.

```
WebSocket Echo Server
        |
PriceWebSocketService
        |
FeedViewModel
        |
UI (Compose)
```

- **PriceWebSocketService** - manages WebSocket connection, exposes messages and connection state as Flow
- **FeedViewModel** - generates random prices, sends to WebSocket, receives echo, updates UI state
- **DetailsViewModel** - uses SavedStateHandle to retrieve the selected symbol from navigation arguments
- **FeedScreen** - displays stock list with LazyColumn, observes StateFlow
- **DetailsScreen** - shows selected symbol details with live price updates
- **AppNavGraph** - Navigation Compose with NavHost, two destinations

### Architecture decisions

**Single WebSocket connection.** FeedViewModel owns the WebSocket stream. Both screens observe the same StateFlow without duplicate connections. DetailsScreen gets live updates because it reads from the same FeedViewModel instance scoped to the NavHost.

**No Hilt/DI.** Intentionally kept simple for this scope. In a production app, Hilt would be used to inject a shared PriceRepository into both ViewModels, allowing DetailsViewModel to observe price updates independently. Current approach shares FeedViewModel via Navigation Compose scoping, which works well for this use case.

**Reconnect logic.** WebSocket automatically reconnects after a few seconds if the connection drops while the feed is active, for example when the app goes to background.

## How it works

1. User taps Start, WebSocket connects to echo server
2. Every 2 seconds, random price changes are generated for each symbol
3. Prices are sent as JSON to the WebSocket server
4. Server echoes the message back
5. ViewModel parses the response and updates StateFlow
6. UI recomposes with new prices, sorted by value
7. Price flash animation highlights rows on change

## Tech Stack

- Kotlin
- Jetpack Compose, 100% Compose UI
- Navigation Compose with NavHost
- ViewModel + StateFlow + SavedStateHandle
- OkHttp WebSocket
- Coroutines and Flow
- Material 3

## Project Structure

```
com.example.pricetracker/
  MainActivity.kt              - entry point
  AppNavGraph.kt                - navigation setup, deep links
  FeedScreen.kt                 - stock list UI with flash animation
  FeedViewModel.kt              - business logic, WebSocket orchestration
  DetailsScreen.kt              - symbol details UI
  DetailsViewModel.kt           - SavedStateHandle for selected symbol
  StockPrice.kt                 - data model
  PriceWebSocketService.kt      - WebSocket client
  ui/theme/Theme.kt             - Material theme
```

## How to Run

1. Clone the repository
2. Open in Android Studio
3. Run on device or emulator (API 24+)
4. Tap Start to begin price feed

You can also test the deep link:
```
adb shell am start -a android.intent.action.VIEW -d "stocks://symbol/AAPL" com.example.pricetracker
```

## Requirements

- Android Studio Ladybug or newer
- Min SDK 24
- Internet connection for WebSocket
