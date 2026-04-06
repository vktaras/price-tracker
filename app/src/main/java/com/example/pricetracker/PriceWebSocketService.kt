package com.example.pricetracker

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class PriceWebSocketService {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    private val _connectionState = Channel<Boolean> (Channel.CONFLATED)
    val connectionState: Flow<Boolean> = _connectionState.receiveAsFlow()

    fun connect() {
        val request = Request.Builder()
            .url("wss://ws.postman-echo.com/raw")
            .build()

        webSocket = client.newWebSocket(request, object :
            WebSocketListener() {

            }
        )
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1000, "User Stopped")
        webSocket = null
    }

}