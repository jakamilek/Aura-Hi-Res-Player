package iad1tya.echo.music.ui.screens.playlist

import androidx.compose.runtime.Composable
import iad1tya.echo.music.db.entities.PlaylistSong

/** AI playlist modification was removed from the privacy fork. */
@Composable
fun AiModifyPlaylistDialog(
    playlistId: String,
    songs: List<PlaylistSong>,
    onDismiss: () -> Unit,
    onOpenAiSettings: () -> Unit,
) {
    // Intentionally render no UI. This compatibility entry point performs no AI operation.
}
