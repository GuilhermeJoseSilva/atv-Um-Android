package com.example.myapplication

import android.os.Bundle
import android.service.notification.NotificationListenerService.RankingMap
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JornadaTela()
                }
            }
        }
    }
}

@Composable
fun JornadaTela() {
    var numClicksNecessary by remember { mutableStateOf(Random.nextInt(1, 51)) }
    var clickAtual by remember { mutableStateOf(0) }
    var imageAtual by remember { mutableStateOf(R.drawable.inicio_da_jornada) }
    var messageConquista by remember { mutableStateOf(false) }
    var messageDesistencia by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Imagem de fundo
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Imagem de fundo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageAtual != R.drawable.desistencia){
                Text(
                    text = "Clique na Imagem",
                    fontSize = 24.sp, // Aumenta o tamanho da fonte
                    fontWeight = FontWeight.Bold, // Aplica negrito ao texto
                    color = Color.Yellow, // Define a cor do texto
                    modifier = Modifier.padding(bottom = 16.dp) // Adiciona espaçamento abaixo do texto
                )
            }

            Image(painter = painterResource(id = imageAtual),
                contentDescription = "Imagem da Jornada",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                    .clickable {
                        clickAtual++
                        imageAtual = when {
                            clickAtual.toFloat() / numClicksNecessary < 0.33 -> R.drawable.inicio_da_jornada
                            clickAtual.toFloat() / numClicksNecessary < 0.66 -> R.drawable.meio_do_caminho
                            clickAtual.toFloat() / numClicksNecessary < 1.0 -> R.drawable.fim_da_jornada
                            else -> R.drawable.comemorando
                        }

                        if (clickAtual >= numClicksNecessary) {
                            messageConquista = true
                        }
                    }
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                imageAtual == R.drawable.desistencia
                messageDesistencia = true
            }) {
                Text("Desistir")
            }

            if (messageConquista) {
                Finaldialogue(
                    finalMessage = "PARABENS PELA CONQUISTA!!!! quer jogar denovo?",
                    onNewGame = {
                    numClicksNecessary = Random.nextInt(1, 51)
                    clickAtual = 0
                    imageAtual = R.drawable.inicio_da_jornada
                        messageConquista = false
                }, onClose = {
                        messageConquista = false
                })
            }

            if (messageDesistencia){
                Finaldialogue(
                    finalMessage = "Que pena que desistiu :(, quer tentar denovo?",
                    onNewGame = {
                        numClicksNecessary = Random.nextInt(1,51)
                        clickAtual = 0
                        imageAtual = R.drawable.desistencia
                        messageDesistencia = false
                    },
                    onClose = { messageDesistencia = false }
                )
            }
        }
    }




    @Composable
    fun Finaldialogue(finalMessage:String, onNewGame: () -> Unit, onClose: () -> Unit) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(text = "Fim da jornada")
            },
            text = {
                Text(text = finalMessage)
            },
            confirmButton = {
                Button(onClick = onNewGame) {
                    Text(text = "Sim")
                }
            },
            dismissButton = {
                Button(onClick = onClose) {
                    Text(text = "Não")
                }
            }
        )
    }
