package cat.itb.m78.trivial.trivial

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    private var selectedQuestions = allQuestions.shuffled().take(settings.numberOfQuestions + 1)

    var currentQuestionIndex by mutableStateOf(0)

    var score by mutableStateOf(0)
        private set

    val currentQuestion = derivedStateOf { selectedQuestions[currentQuestionIndex] }

    val totalRounds: Int
        get() = selectedQuestions.size

    fun nextQuestion(answer: String): Boolean {
        if (answer == currentQuestion.value.correctAnswer) {
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