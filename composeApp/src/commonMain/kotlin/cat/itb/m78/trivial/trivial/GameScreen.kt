package cat.itb.m78.trivial.trivial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswer: String
)

@Composable
fun GameScreen(
    navigateToScreen4: (String) -> Unit,
    viewModel: GameViewModel
) {
    val question = viewModel.currentQuestion.value
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
        Text(question.text, modifier = Modifier.align(Alignment.CenterHorizontally))

        shuffledAnswers.chunked(2).forEach { rowAnswers ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowAnswers.forEach { answer ->
                    Button(
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                        onClick = {
                            viewModel.nextQuestion(answer)

                            if (viewModel.isGameFinished() && viewModel.currentQuestionIndex == viewModel.totalRounds - 1) {
                                viewModel.currentQuestionIndex = 0
                                navigateToScreen4("Your final score is: ${viewModel.score}")
                            }
                        }
                    ) {
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