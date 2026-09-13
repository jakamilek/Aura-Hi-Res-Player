package iad1tya.echo.music.ui.screens.settings.integrations

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBarScrollBehavior

/** Listen Together is intentionally disabled in the privacy fork. */
@Composable
fun ListenTogetherSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Text(
        text = "Listen Together is disabled in this privacy build.",
        color = MaterialTheme.colorScheme.onBackground,
    )
}
