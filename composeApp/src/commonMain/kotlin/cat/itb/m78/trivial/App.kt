package cat.itb.m78.trivial

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cat.itb.m78.trivial.theme.AppTheme
import cat.itb.m78.trivial.trivial.Trivial

@Composable
internal fun App() = AppTheme {
    Trivial()
}
