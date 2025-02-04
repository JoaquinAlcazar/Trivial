import androidx.compose.ui.window.ComposeUIViewController
import cat.itb.m78.trivial.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
