package com.example.pricetracker

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class PriceWebSocketService {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    companion object {
        private const val WS_URL = "wss://ws.postman-echo.com/raw"
        private const val WS_CLOSE_CODE = 1000
    }

    private val _messages = Channel<String>(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    private val _connectionState = Channel<Boolean> (Channel.CONFLATED)
    val connectionState: Flow<Boolean> = _connectionState.receiveAsFlow()

    fun connect() {
        val request = Request.Builder()
            .url(WS_URL)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

                override fun onOpen(webSocket: WebSocket, response: Response) {
                    _connectionState.trySend(true)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    _messages.trySend(text)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    _connectionState.trySend(false)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    _connectionState.trySend(false)
                }

            }
        )
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(WS_CLOSE_CODE, "User Stopped")
        webSocket = null
    }

}