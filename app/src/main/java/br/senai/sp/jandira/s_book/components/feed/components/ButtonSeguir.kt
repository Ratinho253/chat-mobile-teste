package br.senai.sp.jandira.s_book.components.feed.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.s_book.R

@Composable
fun ButtonSeguir(
    text: String,
    onClick: () -> Unit
) {
    Row(modifier = Modifier
        .width(162.dp)
        .height(24.dp)
        .clickable {
            onClick()
        }
        .border(width = 1.dp, color = Color(120, 79, 52), shape = RoundedCornerShape(size = 4.dp))
        .background(Color(255, 255, 255, 1)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text, style = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.intermedium)),
                fontWeight = FontWeight(700),
                color = Color(120, 79, 52),
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonScreen() {
    ButtonSeguir(text = "Seguir  e continuar ",
        onClick = {})
}