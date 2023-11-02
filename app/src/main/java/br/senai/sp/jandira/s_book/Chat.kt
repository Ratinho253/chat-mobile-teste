package br.senai.sp.jandira.s_book

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.s_book.navigation_home_bar.MainScreen
import br.senai.sp.jandira.s_book.ui.theme.SBOOKTheme
import br.senai.sp.jandira.s_book.view_model.AnuncioViewModel
import br.senai.sp.jandira.s_book.view_model.CreateAccountView
import br.senai.sp.jandira.s_book.view_model.ResetPasswordView
import br.senai.sp.jandira.s_book.view_model.UserCategoryViewModel

class Chat : ComponentActivity() {
    private val socketManager = tentando()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SBOOKTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.White
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = "Chat"
                    ) {
                        composable("Chat") {
                            SocketInterfaceComposable(
                                socketManager = tentando(),
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}


////                var message by remember { mutableStateOf("") }
////                var messages by remember { mutableStateOf(listOf<String>()) }
////
////
////                Column(
////                    modifier = Modifier
////                        .fillMaxSize()
////                        .background(Color.Green),
////                    verticalArrangement = Arrangement.Center,
////                    horizontalAlignment = Alignment.CenterHorizontally
////                ) {
////                    Column(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                    ) {
////                        messages.forEach { msg ->
////                            Text(msg)
////                        }
////                    }
////
////                    TextField(
////                        value = message,
////                        onValueChange = { newMessage ->
////                            message = newMessage
////                        },
//                        keyboardOptions = KeyboardOptions.Default.copy(
//                            imeAction = ImeAction.Send
//                        ),
//                        keyboardActions = KeyboardActions(
//                            onSend = {
//                                sendMessage(message)
//                            }
//                        )
////                    )
////                    Spacer(modifier = Modifier.height(5.dp))
////                    DefaultButtonScreen(
////                        text = "Fez a requisição"
////                    ) {
////                        sendMessage(message)
////                        message = ""
////                    }
////                }