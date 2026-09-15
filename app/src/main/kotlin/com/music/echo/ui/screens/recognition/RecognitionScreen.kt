package iad1tya.echo.music.ui.screens.recognition

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/** Recognition is intentionally disabled in the privacy fork. */
@Composable
fun RecognitionScreen(
    navController: NavController,
    autoStart: Boolean = false,
) {
    Text(
        text = "Music recognition is disabled in this privacy build.",
        color = MaterialTheme.colorScheme.onBackground,
    )
}
