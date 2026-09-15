package iad1tya.echo.music.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Privacy-build compatibility screen.
 *
 * External Last.fm and ListenBrainz scrobbling is disabled in this build. The route remains
 * temporarily so restored navigation state cannot crash after upgrading from an older build.
 */
@Composable
fun LastFMSettingsScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Scrobbling wyłączony w buildzie privacy",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "Last.fm i ListenBrainz nie wysyłają danych o odsłuchiwaniu.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}
