package br.senai.sp.jandira.s_book

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.senai.sp.jandira.s_book.ui.theme.SBOOKTheme
import coil.compose.AsyncImage
import com.google.gson.Gson
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

                    ChatScreen(1)
                }
            }
        }
    }
}

@Composable
fun ChatScreen(idUsuario: Int) {
    val client = ChatClient()
    client.connect(idUsuario)
    val socket = client.getSocket()

    var tela by remember {
        mutableStateOf(false)
    }

    var chatViewModel = viewModel<ChatViewModel>()

    if (tela) {
        ConversaScreen(
            socket = socket,
            chatViewModel = chatViewModel,
            idUsuario = idUsuario,
            client = client
        )
    } else {
        ContatosScreen(client = client, idUsuario, socket = socket, chatViewModel) {
            tela = it
        }
    }
}

@Composable
fun ContatosScreen(
    client: ChatClient,
    idUsuario: Int,
    socket: Socket,
    chatViewModel: ChatViewModel,
    onChangeTela: (Boolean) -> Unit
) {
    val TAG = "Teste de socket"

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        var listaContatos by remember {
            mutableStateOf(
                SocketResponse(
                    users = listOf()
                )
            )
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(listaContatos.users) {
                var contato = it.users.filter { user -> user.id != idUsuario }
                Log.e(TAG, "Contato: $contato")

                Cards(nome1 = contato[0].nome, contato[0].foto) {
                    chatViewModel.idChat = it.id_chat
                    chatViewModel.idUser2 = contato[0].id
                    socket.emit("listMessages", it.id_chat)
                    onChangeTela(true)
                }
            }
        }
    }
}

@Composable
fun ConversaScreen(
    client: ChatClient,
    socket: Socket,
    chatViewModel: ChatViewModel,
    idUsuario: Int
) {

    val idChat = chatViewModel.idChat
    val idUser2 = chatViewModel.idUser2

    var message by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        var listaMensagens by remember {
            mutableStateOf(
                MensagensResponse(
                    status = 0,
                    message = "",
                    id_chat = "",
                    usuarios = listOf(),
                    data_criacao = "",
                    hora_criacao = "",
                    mensagens = mutableStateListOf()
                )
            )
        }

//        val mensagem = Mensagem(
//            _id = "",
//            messageBy = 0,
//            messageTo = 0,
//            message = "",
//            image = "",
//            data_criacao = "",
//            hora_criacao = "",
//            chatId = "",
//            __v = null
//        )

        // Ouça o evento do socket
        socket.on("receive_message") { args ->
            args.let { d ->
                if (d.isNotEmpty()) {
                    val data = d[0]
                    if (data.toString().isNotEmpty()) {
                        val mensagens =
                            Gson().fromJson(data.toString(), MensagensResponse::class.java)

                        listaMensagens = mensagens
                        Log.e("TesteIndo", "${listaMensagens.mensagens.reversed()}", )
                    }
                }
            }
        }

        // Ouça o evento do socket
//        socket.on("return_message") { args ->
//            args.let { d ->
//                if (d.isNotEmpty()) {
//                    val data = d[0]
//                    if (data.toString().isNotEmpty()) {
//                        val mensagens =
//                            Gson().fromJson(data.toString(), Mensagem::class.java)
//
//                        list = list + mensagens
//
//                       Log.e("Lista", "Lista: ${listaMensagens.mensagens}" )
//                    }
//                }
//            }
//        }

        LazyColumn(
            modifier = Modifier
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(listaMensagens.mensagens.reversed()) {
                if (it.messageTo == idUsuario) {
                    CardText(
                        mensagem = it.message,
                        hora = it.hora_criacao!!,
                        envio = it.messageBy,
                        cor = Color.DarkGray
                    )
                } else {
                    CardText(
                        mensagem = it.message,
                        hora = it.hora_criacao!!,
                        envio = it.messageBy,
                        cor = Color.LightGray
                    )
                }
            }
        }
    }

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
            },
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(id = R.color.cinza),
                unfocusedBorderColor = colorResource(id = R.color.cinza)
            )
        )
        Button(
            onClick = {

                val json = JSONObject().apply {
                    put("messageBy", idUsuario)
                    put("messageTo", idUser2)
                    put("message", message)
                    put("image", "")
                    put("chatId", idChat)
                }

                Log.e("JSON", "$json", )
               // val jsonString = Json.encodeToString(json)

                client.sendMessage(json)
            },

            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(Color(221, 163, 93, 255))
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_send_24),
                contentDescription = ""
            )
        }

    }
}

@Composable
fun CardText(
    mensagem: String,
    hora: String,
    envio: Int,
    cor: Color
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .background(cor),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),

        ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = mensagem,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF000000)
            )
            Text(
                text = "$envio",
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF3B4A54),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            Text(
                text = hora,
                fontSize = 10.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF3B4A54),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun Cards(
    nome1: String,
    foto: String,
    onClick: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 16.dp
        ),
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = foto,
                contentDescription = ""
            )
            Text(
                text = nome1,
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color(0xFF000000)
            )
//            Text(
//                text = nome2,
//                fontSize = 10.sp,
//                fontWeight = FontWeight(400),
//                color = Color(0xFF3B4A54),
//                modifier = Modifier.fillMaxWidth(),
//                textAlign = TextAlign.End
//            )
        }
    }

}


