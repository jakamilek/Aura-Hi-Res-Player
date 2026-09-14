package iad1tya.echo.music.utils

import android.content.Context
import iad1tya.echo.music.models.MediaMetadata
import kotlinx.coroutines.CoroutineScope

/**
 * Privacy-build compatibility shim.
 * External scrobbling (Last.fm / ListenBrainz) is disabled so playback does not
 * submit listening history or track metadata to third-party services.
 */
class ScrobbleManager(
    private val scope: CoroutineScope,
    var minSongDuration: Int = 30,
    var scrobbleDelayPercent: Float = 0.5f,
    var scrobbleDelaySeconds: Int = 50
) {
    var useNowPlaying = false
    var enableScrobbling = false
    var useSendLikes = false

    // Retained only for compatibility with existing MusicService wiring.
    var listenBrainzEnabled = false
    var listenBrainzToken = ""
    var appContext: Context? = null

    fun destroy() = Unit
    fun onSongStart(metadata: MediaMetadata?, duration: Long? = null) = Unit
    fun onSongResume(metadata: MediaMetadata) = Unit
    fun onSongPause() = Unit
    fun onPlayerStateChanged(isPlaying: Boolean, metadata: MediaMetadata?, duration: Long? = null) = Unit
    fun onSongStop() = Unit
}
