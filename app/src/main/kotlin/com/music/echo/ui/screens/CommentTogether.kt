package iad1tya.echo.music.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/** Listen Together chat removed from the privacy fork. */
@Composable
fun CommentTogetherScreen(navController: NavController) {
    Text(
        text = "Listen Together is disabled in this privacy build.",
        color = MaterialTheme.colorScheme.onSurface,
    )
}
