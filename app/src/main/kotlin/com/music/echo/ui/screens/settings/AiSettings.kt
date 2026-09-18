package iad1tya.echo.music.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.navigation.NavController

/** AI providers were removed from the privacy fork. */
@Composable
fun AiSettings(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "AI providers are disabled in this privacy build.",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
