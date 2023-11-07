package br.senai.sp.jandira.s_book

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import br.senai.sp.jandira.s_book.ui.theme.SBOOKTheme
import coil.compose.AsyncImage
import com.google.firebase.firestore.local.LocalStore
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
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
                    val client = ChatClient()
                    client.connect("Luiz")

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

    val socket = client.getSocket()


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

//    Column(
//        modifier = Modifier
//            .padding(24.dp)
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState()),
//        verticalArrangement = Arrangement.SpaceBetween,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Spacer(modifier = Modifier.height(16.dp))
//        //response = socket.on("receive_contacts"){}
//
//        // Ouça o evento do socket
//        socket.on("receive_contacts") { args ->
//            args.let { d ->
//                if (d.isNotEmpty()) {
//                    val data = d[0]
//                    if (data.toString().isNotEmpty()) {
//                        val chat = Gson().fromJson(data.toString(), SocketResponse::class.java)
//
//
//
//                        for (chatUser in chat.users) {
//                            cards(nome1 = chatUser.users[0].nome, nome2 = chatUser.users[1].nome)
//
//                        }
//
//                        Log.e("ChatDebug", "$chat")
//                    }
//                }
//            }
//        }
//
//
////        Spacer(modifier = Modifier.height(16.dp))
////        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
////            OutlinedTextField(
////                value = message,
////                onValueChange = { newMessage ->
////                    message = newMessage
////                },
////                keyboardOptions = KeyboardOptions.Default.copy(
////                    imeAction = ImeAction.Send
////                ),
////                keyboardActions = KeyboardActions(
////                    onSend = {
////                        client.sendMessage(message)
////                        message = ""
////                    }
////                ),
////                shape = RoundedCornerShape(50.dp),
////                colors = TextFieldDefaults.outlinedTextFieldColors(
////                    focusedBorderColor = colorResource(id = R.color.cinza),
////                    unfocusedBorderColor = colorResource(id = R.color.cinza)
////                )
////            )
////            Button(
////                onClick = {
////                    client.sendMessage(message)
////                    message = ""
////                },
////                modifier = Modifier.size(50.dp),
////                shape = CircleShape,
////            ) {
////                Icon(
////                    painter = painterResource(id = R.drawable.baseline_send_24),
////                    contentDescription = ""
////                )
////            }
////        }
//    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        var listaContatos by remember {
            mutableStateOf(SocketResponse(
                users = listOf()
            ))
        }
        // Ouça o evento do socket
        socket.on("receive_contacts") { args ->
            args.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    if (data.toString().isNotEmpty()) {
                        val chat = Gson().fromJson(data.toString(), SocketResponse::class.java)

                        listaContatos = chat
                    }
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(listaContatos.users){
                cards(nome1 = it.users[0].nome, nome2 = it.users[1].nome)
            }
        }
    }



}

@Composable
fun cards (
    nome1: String,
    nome2: String
){

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
//                                         AsyncImage(
//                                             model = it.users[0].foto ,
//                                             contentDescription =""
//                                         )
            Text(
                text = nome1,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF000000)
            )
            Text(
                text = nome2,
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF3B4A54),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }

}


