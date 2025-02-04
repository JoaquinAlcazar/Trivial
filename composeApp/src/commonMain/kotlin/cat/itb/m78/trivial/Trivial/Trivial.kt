import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Definición de la pregunta y las respuestas
data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswer: String
)

// ViewModel para gestionar el estado de las preguntas en Screen3
class GameViewModel : ViewModel() {

    private val questions = listOf(
        Question("What is the capital of France?", listOf("Paris", "Madrid", "Berlin", "Rome"), "Paris"),
        Question("What is 2 + 2?", listOf("3", "4", "5", "6"), "4"),
        Question("Who wrote 'Hamlet'?", listOf("Shakespeare", "Cervantes", "Homer", "Goethe"), "Shakespeare"),
        Question("Which planet is known as the Red Planet?", listOf("Earth", "Venus", "Mars", "Jupiter"), "Mars")
    )

    var currentQuestionIndex by mutableStateOf(0)
        private set

    var score by mutableStateOf(0)

    val currentQuestion: Question
        get() = questions[currentQuestionIndex]

    val totalRounds: Int
        get() = questions.size

    fun nextQuestion(answer: String) {
        if (answer == currentQuestion.correctAnswer) {
            score++
        }
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        }
    }

    fun isGameFinished(): Boolean {
        return currentQuestionIndex >= questions.size
    }
}



@Composable
fun Trivial() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "screen1"
    ) {
        composable("screen1") {
            Screen1(
                navigateToScreen2 = { navController.navigate("screen2") },
                navigateToScreen3 = { navController.navigate("screen3") }
            )
        }
        composable("screen2") {
            Screen2(
                navigateToScreen1 = { navController.popBackStack("screen1", inclusive = false) }
            )
        }
        composable("screen3") {
            val viewModel = viewModel { GameViewModel() }
            Screen3(
                navigateToScreen4 = { message ->
                    navController.navigate("screen4/$message")
                },
                viewModel = viewModel
            )
        }
        composable("screen4/{message}") { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message") ?: ""
            Screen4(
                navigateToScreen1 = { navController.popBackStack("screen1", inclusive = false) },
                message = message
            )
        }
    }
}


@Composable
fun Screen1(
    navigateToScreen2: () -> Unit,
    navigateToScreen3: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = navigateToScreen2) {
            Text("Settings (Screen 2)")
        }
        Button(onClick = navigateToScreen3) {
            Text("Game (Screen 3)")
        }
    }
}

@Composable
fun Screen2(navigateToScreen1: () -> Unit) {
}

@Composable
fun Screen3(
    navigateToScreen4: (String) -> Unit,  // Ahora recibe un mensaje (puntuación)
    viewModel: GameViewModel
) {
    val question = viewModel.currentQuestion

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Round: ${viewModel.currentQuestionIndex + 1} / ${viewModel.totalRounds}")
        Spacer(modifier = Modifier.weight(1f))
        Text(question.text)

        // Mostrar las respuestas en un formato de filas
        question.answers.chunked(2).forEach { rowAnswers ->
            Row {
                rowAnswers.forEach { answer ->
                    Button(onClick = {
                        // Avanzamos a la siguiente pregunta
                        viewModel.nextQuestion(answer)

                        // Si el juego ha terminado, navegamos a la pantalla final
                        if (viewModel.isGameFinished()) {
                            navigateToScreen4("Your final score is: ${viewModel.score}")
                        }
                    }) {
                        Text(answer)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}



@Composable
fun Screen4(navigateToScreen1: () -> Unit, message: String) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Game Over!")
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)  // Mostramos el mensaje (puntuación final)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreen1) {
            Text("Go to Home")
        }
    }
}

