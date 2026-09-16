package iad1tya.echo.music.ui.menu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import iad1tya.echo.music.LocalDatabase
import iad1tya.echo.music.R
import iad1tya.echo.music.db.entities.LyricsEntity
import iad1tya.echo.music.db.entities.SongEntity
import iad1tya.echo.music.models.MediaMetadata
import iad1tya.echo.music.ui.component.Material3MenuGroup
import iad1tya.echo.music.ui.component.Material3MenuItemData
import iad1tya.echo.music.ui.component.NewAction
import iad1tya.echo.music.ui.component.NewActionGrid
import iad1tya.echo.music.ui.component.TextFieldDialog

@Composable
fun LyricsMenu(
    lyricsProvider: () -> LyricsEntity?,
    songProvider: () -> SongEntity?,
    mediaMetadataProvider: () -> MediaMetadata,
    onDismiss: () -> Unit,
    onShowOffsetDialog: () -> Unit = {},
) {
    val database = LocalDatabase.current
    var showEditDialog by rememberSaveable { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(songProvider()?.romanizeLyrics ?: true) }
    val lyricsOffset = songProvider()?.lyricsOffset ?: 0

    LaunchedEffect(songProvider()) {
        isChecked = songProvider()?.romanizeLyrics ?: true
    }

    if (showEditDialog) {
        TextFieldDialog(
            onDismiss = { showEditDialog = false },
            icon = { Icon(painter = painterResource(R.drawable.edit), contentDescription = null) },
            title = { Text(text = mediaMetadataProvider().title) },
            initialTextFieldValue = TextFieldValue(lyricsProvider()?.lyrics.orEmpty()),
            singleLine = false,
            onDone = { text ->
                database.query {
                    upsert(
                        LyricsEntity(
                            id = mediaMetadataProvider().id,
                            lyrics = text,
                            provider = lyricsProvider()?.provider ?: "Manual",
                            userEdited = true,
                        ),
                    )
                }
            },
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(
            bottom = 8.dp + WindowInsets.systemBars.asPaddingValues().calculateBottomPadding(),
        ),
    ) {
        item {
            NewActionGrid(
                actions = listOf(
                    NewAction(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.edit),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        text = stringResource(R.string.edit),
                        onClick = { showEditDialog = true },
                    ),
                    NewAction(
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.fast_forward),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        text = stringResource(R.string.lyrics_offset),
                        onClick = {
                            onDismiss()
                            onShowOffsetDialog()
                        },
                    ),
                ),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 16.dp),
            )
        }

        item {
            Material3MenuGroup(
                items = listOf(
                    Material3MenuItemData(
                        title = { Text(stringResource(R.string.lyrics_offset)) },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.fast_forward),
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            onDismiss()
                            onShowOffsetDialog()
                        },
                        trailingContent = {
                            Text(
                                text = "${if (lyricsOffset >= 0) "+" else ""}${lyricsOffset}ms",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                    ),
                    Material3MenuItemData(
                        title = { Text(stringResource(R.string.romanize_current_track)) },
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.language_korean_latin),
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            isChecked = !isChecked
                            songProvider()?.let { song ->
                                database.query { upsert(song.copy(romanizeLyrics = isChecked)) }
                            }
                        },
                        trailingContent = {
                            Switch(
                                checked = isChecked,
                                onCheckedChange = { enabled ->
                                    isChecked = enabled
                                    songProvider()?.let { song ->
                                        database.query { upsert(song.copy(romanizeLyrics = enabled)) }
                                    }
                                },
                            )
                        },
                    ),
                ),
            )
        }
    }
}
