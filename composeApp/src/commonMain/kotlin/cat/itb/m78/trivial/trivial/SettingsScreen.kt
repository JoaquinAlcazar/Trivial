package cat.itb.m78.trivial.trivial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(navigateToScreen1: () -> Unit) {
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