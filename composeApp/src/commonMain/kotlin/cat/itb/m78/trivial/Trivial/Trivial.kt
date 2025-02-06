import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
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
import kotlinx.coroutines.delay
import m78exercices.composeapp.generated.resources.Res
import m78exercices.composeapp.generated.resources.TrivialLogo
import org.jetbrains.compose.resources.painterResource

data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswer: String
)

data object settings {
    var difficulty by mutableStateOf(0)
    var numberOfQuestions by mutableStateOf(5)
}


class GameViewModel : ViewModel() {

    private val allQuestions = listOf(
        Question("What is the capital of France?", listOf("Paris", "Madrid", "Berlin", "Rome"), "Paris"),
        Question("What is 2 + 2?", listOf("3", "4", "5", "6"), "4"),
        Question("Who wrote 'Hamlet'?", listOf("Shakespeare", "Cervantes", "Homer", "Goethe"), "Shakespeare"),
        Question("Which planet is known as the Red Planet?", listOf("Earth", "Venus", "Mars", "Jupiter"), "Mars"),
        Question("What is the capital of Spain?", listOf("Paris", "Madrid", "Berlin", "Rome"), "Madrid"),
        Question("What is 3 + 3?", listOf("3", "4", "5", "6"), "6"),
        Question("Who wrote 'Don Quijote'?", listOf("Shakespeare", "Cervantes", "Homer", "Goethe"), "Cervantes"),
        Question("Which planet is known as the Blue Planet?", listOf("Earth", "Venus", "Mars", "Jupiter"), "Earth"),
        Question("What is the capital of Germany?", listOf("Paris", "Madrid", "Berlin", "Rome"), "Berlin"),
        Question("What is 4 + 4?", listOf("3", "4", "5", "8"), "8"),
        Question("Who wrote 'The Iliad'?", listOf("Shakespeare", "Cervantes", "Homer", "Goethe"), "Homer"),
        Question("Which planet is known as the Green Planet?", listOf("Earth", "Venus", "Mars", "Jupiter"), "Venus"),
        Question("What is the capital of Italy?", listOf("Paris", "Madrid", "Berlin", "Rome"), "Rome"),
        Question("What is 5 + 5?", listOf("3", "4", "5", "10"), "10"),
        Question("Who wrote 'Faust'?", listOf("Shakespeare", "Cervantes", "Homer", "Goethe"), "Goethe"),
    )

    private var selectedQuestions = allQuestions.shuffled().take(settings.numberOfQuestions + 1)

    var currentQuestionIndex by mutableStateOf(0)

    var score by mutableStateOf(0)
        private set

    val currentQuestion: Question
        get() = selectedQuestions[currentQuestionIndex]

    val totalRounds: Int
        get() = selectedQuestions.size

    fun nextQuestion(answer: String): Boolean {
        if (answer == currentQuestion.correctAnswer) {
            score++
        }

        if (currentQuestionIndex >= selectedQuestions.size - 1) {
            return true
        }

        currentQuestionIndex++
        return false
    }

    fun isGameFinished(): Boolean {
        return currentQuestionIndex >= selectedQuestions.size - 1
    }

    fun restartGame() {
        selectedQuestions = allQuestions.shuffled().take(settings.numberOfQuestions)
        currentQuestionIndex = 0
        score = 0
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
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(Res.drawable.TrivialLogo),
            contentDescription = "Trivial logo",
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreen2) {
            Text("Settings")
        }
        Button(onClick = navigateToScreen3) {
            Text("Start Game")
        }
    }
}

@Composable
fun Screen2(navigateToScreen1: () -> Unit) {
    val offset = 1
    var expanded by remember { mutableStateOf(false) }

    val difficulties = listOf("Easy", "Normal", "Hard")

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Settings")
            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown para seleccionar la dificultad
            Text("Difficulty:")
            Box {
                Button(onClick = { expanded = true }) {
                    Text(difficulties[settings.difficulty])
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    difficulties.forEachIndexed { index, difficulty ->
                        DropdownMenuItem(
                            text = { Text(difficulty) },
                            onClick = {
                                settings.difficulty = index
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selección del número de preguntas
            Text("Number of Questions:")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = settings.numberOfQuestions == 5,
                    onCheckedChange = {
                        if (it) {
                            settings.numberOfQuestions = 5
                        }
                    }
                )
                Text("5 Questions")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = settings.numberOfQuestions == 10,
                    onCheckedChange = {
                        if (it) {
                            settings.numberOfQuestions = 10
                        }
                    }
                )
                Text("10 Questions")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = settings.numberOfQuestions == 15,
                    onCheckedChange = {
                        if (it) {
                            settings.numberOfQuestions = 15
                        }
                    }
                )
                Text("15 Questions")
            }
        }

        // Botón para volver al menú
        Button(
            onClick = navigateToScreen1,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Back to menu")
        }
    }
}

@Composable
fun Screen3(
    navigateToScreen4: (String) -> Unit,
    viewModel: GameViewModel
) {
    val question = viewModel.currentQuestion
    val shuffledAnswers = remember(question) { question.answers.shuffled() } // Mezclar las respuestas cada vez que cambie la pregunta

    val sliderDuration = when (settings.difficulty) {
        0 -> 10_000L
        1 -> 6_000L
        2 -> 4_000L
        else -> 6_000L
    }

    val sliderSteps = 100
    val stepDuration = sliderDuration / sliderSteps

    var sliderValue by remember { mutableStateOf(1f) }

    LaunchedEffect(key1 = viewModel.currentQuestionIndex) {
        sliderValue = 1f
        for (i in 1..sliderSteps) {
            delay(stepDuration)
            sliderValue = 1f - (i.toFloat() / sliderSteps)
        }

        viewModel.nextQuestion("")
        if (viewModel.isGameFinished() && viewModel.currentQuestionIndex == viewModel.totalRounds - 1) {
            viewModel.currentQuestionIndex = 0
            navigateToScreen4("Your final score is: ${viewModel.score}")
        }
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Round: ${viewModel.currentQuestionIndex + 1} / ${settings.numberOfQuestions}")
        Spacer(modifier = Modifier.weight(1f))
        Text(question.text)

        shuffledAnswers.chunked(2).forEach { rowAnswers ->
            Row {
                rowAnswers.forEach { answer ->
                    Button(onClick = {
                        viewModel.nextQuestion(answer)

                        if (viewModel.isGameFinished() && viewModel.currentQuestionIndex == viewModel.totalRounds - 1) {
                            viewModel.currentQuestionIndex = 0
                            navigateToScreen4("Your final score is: ${viewModel.score}")
                        }
                    }) {
                        Text(answer)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = sliderValue,
            onValueChange = {},
            valueRange = 0f..1f,
            enabled = false,
            onValueChangeFinished = {}
        )

        Spacer(modifier = Modifier.weight(1f))
        Text("Score: ${viewModel.score}")
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
        Text(message)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = navigateToScreen1) {
            Text("Go to Home")
        }
    }
}

