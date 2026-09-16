package iad1tya.echo.music.ui.component

import androidx.compose.runtime.Composable

/** AI playlist generation was removed from the privacy fork. */
@Composable
fun AiPlaylistDialog(
    onDismiss: () -> Unit,
    onPlaylistCreated: (String) -> Unit,
    onOpenAiSettings: () -> Unit,
) {
    onDismiss()
}
