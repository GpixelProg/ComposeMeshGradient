import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

/**
 * Run Compose animation
 */

fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = rememberWindowState()) {
        BackgroundAnim()
    }
}