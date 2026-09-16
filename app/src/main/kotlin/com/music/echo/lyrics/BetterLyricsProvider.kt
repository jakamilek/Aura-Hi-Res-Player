package iad1tya.echo.music.lyrics

import android.content.Context

object BetterLyricsProvider : LyricsProvider {
    override val name = "BetterLyrics"
    override fun isEnabled(context: Context): Boolean = false
    override suspend fun getLyrics(id: String, title: String, artist: String, duration: Int, album: String?): Result<String> =
        Result.failure(UnsupportedOperationException("Remote lyrics provider disabled"))
    override suspend fun getAllLyrics(id: String, title: String, artist: String, duration: Int, album: String?, callback: (String) -> Unit) = Unit
}
