package br.senai.sp.jandira.s_book

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SocketInterfaceComposable(
    socketManager: tentando,
    navController: NavController
    ) {
    var messageText by remember { mutableStateOf("") }
    var receivedMessage by remember { mutableStateOf("") }

    val isConnected = socketManager.isConnected()

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Connection management
        Button(
            onClick = {
                if (!isConnected) {
                    socketManager.connect()
                } else {
                    socketManager.disconnect()
                }
            }
        ) {
            Text(text = if (isConnected) "Disconnect" else "Connect")
        }

        // Message input
        BasicTextField(
            value = messageText,
            onValueChange = { messageText = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    socketManager.sendMessage(messageText) // Use a instância de SocketManager para chamar sendMessage
                    messageText = "" // Limpar o campo de texto após o envio
                }
            )
        )

        // Restante do código Composable

        Text(text = "Received Message: $receivedMessage")
    }
}