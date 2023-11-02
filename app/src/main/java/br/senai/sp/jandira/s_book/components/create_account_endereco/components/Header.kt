package br.senai.sp.jandira.s_book.components.create_account_endereco.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
@Composable
fun Header(){

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(5.dp)
    ) {
        Column(){
            Spacer(modifier = Modifier.height(13.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp),
            ) {
                Text(
                    text = "Criar Conta ",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight(800),
                        color = Color(0xFF000000),
                    )
                )

                Text(
                    text = "Seja bem vindo",
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFF000000),
                    )
                )
            }
        }
    }
}