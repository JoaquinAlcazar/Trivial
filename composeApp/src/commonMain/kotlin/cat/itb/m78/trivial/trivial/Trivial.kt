package cat.itb.m78.trivial.trivial

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class Question(
    val text: String,
    val answers: List<String>,
    val correctAnswer: String
)

data object settings {
    var difficulty by mutableStateOf(0)
    var numberOfQuestions by mutableStateOf(5)
}


@Composable
fun Trivial() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "screen1"
    ) {
        composable("screen1") {
            MainMenu(
                navigateToScreen2 = { navController.navigate("screen2") },
                navigateToScreen3 = { navController.navigate("screen3") }
            )
        }
        composable("screen2") {
            SettingsScreen(
                navigateToScreen1 = { navController.popBackStack("screen1", inclusive = false) }
            )
        }
        composable("screen3") {
            val viewModel = viewModel { GameViewModel() }
            GameScreen(
                navigateToScreen4 = { message ->
                    navController.navigate("screen4/$message")
                },
                viewModel = viewModel
            )
        }

        composable("screen4/{message}") { backStackEntry ->
            val message = backStackEntry.arguments?.getString("message") ?: ""
            ScoreScreen(
                navigateToScreen1 = { navController.popBackStack("screen1", inclusive = false) },
                message = message
            )
        }
    }
}









