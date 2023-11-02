package br.senai.sp.jandira.s_book.components.testes_web_socket

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.s_book.R
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.ImeAction


class MainActivity : ComponentActivity() {

    private val socket: Socket by lazy {
        IO.socket("http://26.166.70.79:8080")
    }

    init {
        socket.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var message by remember { mutableStateOf("") }
            var messages by remember { mutableStateOf(listOf<String>()) }


            Column(
                modifier = Modifier.fillMaxSize()
                    .background(Color.Green),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.Cyan)
                ) {
                    messages.forEach { msg ->
                        Text(msg)
                    }
                }

                TextField(
                    value = message,
                    onValueChange = { newMessage ->
                        message = newMessage
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            sendMessage(message)
                            message = ""
                        }
                    )
                )
            }
        }
    }

    private fun sendMessage(message: String) {
        socket.emit("message", message)
        Log.e("testando_chat","${message}")
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}