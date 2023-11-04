package br.senai.sp.jandira.s_book

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.senai.sp.jandira.s_book.ui.theme.SBOOKTheme
import com.google.firebase.firestore.local.LocalStore
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

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

                    val context = LocalContext.current
                    val localStorage = Storage()
                    val client = ChatClient("Luiz", localStorage,context)
                    client.connect()

                    ChatScreen(client, localStorage)
                }
            }
        }
    }
}



@Composable
fun ChatScreen(client: ChatClient, localStore: Storage) {
    val TAG = "Teste de socket"

    var conts by remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        var contacts= ""

        val contatos = client.receiveContacts {
            contacts = it
            conts = contacts
          //  Log.e("Teste muryllo", contacts)
        }

        Log.e(TAG, "$contacts", )
        Log.e(TAG, "$conts", )
    }

    val context = LocalContext.current

    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf<String>()) }


//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Green),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(400.dp)
//                .background(Color.Cyan)
//        ) {
//            messages.forEach { msg ->
//                androidx.compose.material.Text(msg)
//            }
//        }
//
//        TextField(
//            value = message,
//            onValueChange = { newMessage ->
//                message = newMessage
//            },
//            keyboardOptions = KeyboardOptions.Default.copy(
//                imeAction = ImeAction.Send
//            ),
//            keyboardActions = KeyboardActions(
//                onSend = {
//                    //sendMessage(message)
//                    //message = ""
//                }
//            )
//        )
//
//        Button(
//            onClick = {
//                //sendMessage("foio")
//                client.sendMessage(message)
//            },
//            modifier = Modifier
//                .background(Color.Red)
//        ) {}
//    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxSize()
            .verticalScroll(
                ScrollState(0)
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                modifier = Modifier.width(280.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Olá, tudo bem? Eu gostaria de trocar esse livro anúnciado",
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000)
                    )
                    Text(
                        text = "18:13",
                        fontSize = 10.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF3B4A54),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Card(
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ),
                    modifier = Modifier.width(280.dp),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        messages.forEach { msg ->
                            androidx.compose.material.Text(msg)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedTextField(
                value = message,
                onValueChange = { newMessage ->
                    message = newMessage
                },
//                trailingIcon = {
//                    IconButton(onClick = { }) {
//                        Icon(
//                            imageVector = Icons.Default.Image,
//                            contentDescription = "",
//                            tint = Color.Black
//                        )
//                    }
//
//                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        client.sendMessage(message)
                        message = ""
                    }
                ),
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.cinza),
                    unfocusedBorderColor = colorResource(id = R.color.cinza)
                )
            )
            Button(
                onClick = {
                    client.sendMessage(message)
                    message = ""
                },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_send_24),
                    contentDescription = ""
                )
            }
        }
    }
}



