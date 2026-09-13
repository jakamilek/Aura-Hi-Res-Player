package iad1tya.echo.music.utils.qobuz

/**
 * Compatibility layer for legacy call sites that used the removed third-party Qobuz proxy.
 *
 * This class deliberately performs NO network I/O. The privacy fork's only Qobuz network client is
 * iad1tya.echo.music.qobuz.QobuzApi, which talks directly to the official Qobuz API using the user's
 * own credentials. Legacy proxy callers therefore fail closed instead of contacting an external proxy.
 */
@Deprecated("Third-party Qobuz proxy removed; use QobuzHiRes/QobuzApi")
class QobuzApiClient {
    suspend fun search(@Suppress("UNUSED_PARAMETER") query: String): QobuzSearchData =
        QobuzSearchData()

    suspend fun getFileUrl(@Suppress("UNUSED_PARAMETER") trackId: Long): LegacyFileUrl =
        LegacyFileUrl(null)
}

data class LegacyFileUrl(val url: String?)

typealias QobuzSearchData = iad1tya.echo.music.qobuz.QobuzSearchData
typealias QobuzTrack = iad1tya.echo.music.qobuz.QobuzTrack
