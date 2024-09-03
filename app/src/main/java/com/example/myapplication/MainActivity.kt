package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random

// ViewModel para gerenciar o estado do jogo
class GameViewModel : ViewModel() {
    var numClicksNecessary by mutableStateOf(Random.nextInt(1, 51))
    var clickAtual by mutableStateOf(0)
    var imageAtual by mutableStateOf(R.drawable.inicio_da_jornada)
    var messageConquista by mutableStateOf(false)
    var messageDesistencia by mutableStateOf(false)

    fun onImageClick() {
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

    fun onDesistir() {
        imageAtual = R.drawable.desistencia
        messageDesistencia = true
    }

    fun restartGame() {
        numClicksNecessary = Random.nextInt(1, 51)
        clickAtual = 0
        imageAtual = R.drawable.inicio_da_jornada
        messageConquista = false
    }

    fun restartGameFromDesistencia() {
        numClicksNecessary = Random.nextInt(1, 51)
        clickAtual = 0
        imageAtual = R.drawable.desistencia
        messageDesistencia = false
    }

    fun closeMessages() {
        messageConquista = false
        messageDesistencia = false
    }
}

// MainActivity é a classe principal da aplicação, que herda de ComponentActivity.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o conteúdo da tela usando Jetpack Compose.
        setContent {
            MyApplicationTheme {
                // Define um Surface como a tela principal.
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

// Função composable que define o layout e a lógica da tela principal.
@Composable
fun JornadaTela(viewModel: GameViewModel = viewModel()) {
    // Obtém os estados diretamente do ViewModel
    val numClicksNecessary = viewModel.numClicksNecessary
    val clickAtual = viewModel.clickAtual
    val imageAtual = viewModel.imageAtual
    val messageConquista = viewModel.messageConquista
    val messageDesistencia = viewModel.messageDesistencia

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
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
        if (imageAtual != R.drawable.desistencia) {
            Text(
                text = "Clique na Imagem",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Image(
            painter = painterResource(id = imageAtual),
            contentDescription = "Imagem da Jornada",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp))
                .clickable {
                    viewModel.onImageClick()
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            viewModel.onDesistir()
        }) {
            Text("Desistir")
        }

        if (messageConquista) {
            Finaldialogue(
                finalMessage = "PARABENS PELA CONQUISTA!!!! quer jogar denovo?",
                onNewGame = { viewModel.restartGame() },
                onClose = { viewModel.closeMessages() }
            )
        }

        if (messageDesistencia) {
            Finaldialogue(
                finalMessage = "Que pena que desistiu :(, quer tentar denovo?",
                onNewGame = { viewModel.restartGameFromDesistencia() },
                onClose = { viewModel.closeMessages() }
            )
        }
    }
}

// Função composable que exibe um AlertDialog com mensagens e opções de reiniciar ou fechar.
@Composable
fun Finaldialogue(finalMessage: String, onNewGame: () -> Unit, onClose: () -> Unit) {
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
