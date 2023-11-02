package br.senai.sp.jandira.s_book.components.testes_web_socket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@Preview(showSystemUi = true)
@Composable
fun screen1() {
    var inputText by remember { mutableStateOf("") }
    var chatText by remember { mutableStateOf("") }

    val client = OkHttpClient()
    val request = Request.Builder().url("ws://your.websocket.server").build()

    val webSocketListener = object : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            chatText = "$chatText\n$text"
        }
    }

    DisposableEffect(Unit) {
        val webSocket = client.newWebSocket(request, webSocketListener)

        onDispose {
            webSocket.close(1000, "Goodbye")
        }
    }

    Column {
        BasicTextField(
            value = inputText,
            onValueChange = { newValue ->
                inputText = newValue
            },
            singleLine = true,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                runBlocking {
                    chatText = "$chatText\nYou: $inputText"
                    client.newWebSocket(request, webSocketListener).send(inputText)
                }
                inputText = ""
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Enviar")
        }

        Text(
            text = chatText,
            modifier = Modifier.padding(16.dp)
        )
    }
}

