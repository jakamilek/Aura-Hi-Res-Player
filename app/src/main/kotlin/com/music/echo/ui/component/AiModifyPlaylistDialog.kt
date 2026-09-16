package iad1tya.echo.music.ui.component

import androidx.compose.runtime.Composable

/** AI playlist modification was removed from the privacy fork. */
@Composable
fun <T> AiModifyPlaylistDialog(
    playlistId: String,
    songs: List<T>,
    onDismiss: () -> Unit,
    onOpenAiSettings: () -> Unit,
) {
    onDismiss()
}
