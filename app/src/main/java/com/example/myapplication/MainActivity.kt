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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.random.Random

// MainActivity é a classe principal da aplicação, que herda de ComponentActivity.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Define o conteúdo da tela usando Jetpack Compose.
        setContent {
            // Aplica o tema da aplicação.
            MyApplicationTheme {
                // Define um Surface como a tela principal.
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Chama a função composable JornadaTela para definir o conteúdo da tela.
                    JornadaTela()
                }
            }
        }
    }
}

// Função composable que define o layout e a lógica da tela principal.
@Composable
fun JornadaTela() {
    // Estados para gerenciar o número de cliques necessários, cliques atuais, imagem atual e mensagens de conquista/desistência.
    var numClicksNecessary by remember { mutableStateOf(Random.nextInt(1, 51)) }
    var clickAtual by remember { mutableStateOf(0) }
    var imageAtual by remember { mutableStateOf(R.drawable.inicio_da_jornada) }
    var messageConquista by remember { mutableStateOf(false) }
    var messageDesistencia by remember { mutableStateOf(false) }

    // Box é usado para posicionar a imagem de fundo.
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Exibe a imagem de fundo que preenche toda a tela.
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Imagem de fundo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Column é usado para organizar os elementos verticalmente.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Exibe uma mensagem se a imagem atual não for a de desistência.
        if (imageAtual != R.drawable.desistencia){
            Text(
                text = "Clique na Imagem",
                fontSize = 24.sp, // Tamanho da fonte
                fontWeight = FontWeight.Bold, // Negrito
                color = Color.Yellow, // Cor do texto
                modifier = Modifier.padding(bottom = 16.dp) // Espaçamento abaixo do texto
            )
        }

        // Exibe a imagem atual e a torna clicável.
        Image(
            painter = painterResource(id = imageAtual),
            contentDescription = "Imagem da Jornada",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp) // Tamanho da imagem
                .clip(RoundedCornerShape(16.dp)) // Bordas arredondadas
                .border(4.dp, Color.Black, RoundedCornerShape(16.dp)) // Borda preta
                .clickable {
                    // Atualiza o número de cliques e a imagem com base na proporção dos cliques realizados.
                    clickAtual++
                    imageAtual = when {
                        clickAtual.toFloat() / numClicksNecessary < 0.33 -> R.drawable.inicio_da_jornada
                        clickAtual.toFloat() / numClicksNecessary < 0.66 -> R.drawable.meio_do_caminho
                        clickAtual.toFloat() / numClicksNecessary < 1.0 -> R.drawable.fim_da_jornada
                        else -> R.drawable.comemorando
                    }

                    // Se o número de cliques atingir ou exceder o necessário, exibe a mensagem de conquista.
                    if (clickAtual >= numClicksNecessary) {
                        messageConquista = true
                    }
                }
        )

        // Espaço entre a imagem e o botão.
        Spacer(modifier = Modifier.height(24.dp))

        // Botão para desistir do jogo.
        Button(onClick = {
            imageAtual = R.drawable.desistencia
            messageDesistencia = true
        }) {
            Text("Desistir")
        }

        // Exibe uma caixa de diálogo de conquista se a variável messageConquista for verdadeira.
        if (messageConquista) {
            Finaldialogue(
                finalMessage = "PARABENS PELA CONQUISTA!!!! quer jogar denovo?",
                onNewGame = {
                    // Reinicia o jogo com novos parâmetros.
                    numClicksNecessary = Random.nextInt(1, 51)
                    clickAtual = 0
                    imageAtual = R.drawable.inicio_da_jornada
                    messageConquista = false
                }, onClose = {
                    messageConquista = false
                }
            )
        }

        // Exibe uma caixa de diálogo de desistência se a variável messageDesistencia for verdadeira.
        if (messageDesistencia) {
            Finaldialogue(
                finalMessage = "Que pena que desistiu :(, quer tentar denovo?",
                onNewGame = {
                    // Reinicia o jogo com novos parâmetros e define a imagem de desistência.
                    numClicksNecessary = Random.nextInt(1, 51)
                    clickAtual = 0
                    imageAtual = R.drawable.desistencia
                    messageDesistencia = false
                },
                onClose = { messageDesistencia = false }
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
