package br.senai.sp.jandira.s_book

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.s_book.ui.theme.SBOOKTheme
import io.socket.client.IO
import io.socket.client.Socket

class ChatSocketIO : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SBOOKTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen()
                }
            }
        }
    }
}

@Composable
fun ChatScreen(){
    val TAG = "Teste de socket"

    val client = ChatClient("Luiz")
    client.connect()
//    val socket: Socket = IO.socket("http://26.166.70.79:3001")
//
//    socket.connect()
//
//    fun sendMessage(message: String) {
//        Log.e(TAG, "sendMessage: $message", )
//        socket.emit("message", message)
//    }

    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<String>()) }


    Column(
        modifier = Modifier
            .fillMaxSize()
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
                androidx.compose.material.Text(msg)
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
                    //sendMessage(message)
                    //message = ""
                }
            )
        )

        Button(
            onClick = {
                //sendMessage("foio")
                client.sendMessage(message)
            },
            modifier = Modifier
                .background(Color.Red)
        ) {}
    }
}

