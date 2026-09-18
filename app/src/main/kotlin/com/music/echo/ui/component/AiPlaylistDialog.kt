package iad1tya.echo.music.ui.component

import androidx.compose.runtime.Composable

/** AI playlist generation was removed from the privacy fork. */
@Composable
fun AiPlaylistDialog(
    onDismiss: () -> Unit,
    onPlaylistCreated: (String) -> Unit,
    onOpenAiSettings: () -> Unit,
) {
    // Intentionally render no UI. This compatibility entry point performs no AI operation.
}
