package com.aura.migration.source.deezer

import com.aura.migration.model.SourceArtist
import com.aura.migration.model.SourcePlaylist
import com.aura.migration.model.SourceTrack
import com.aura.migration.model.SourceType
import com.aura.migration.source.PlaylistSource
import com.aura.migration.source.SourceError
import com.aura.migration.net.HttpJson

/**
 * Deezer migration compatibility stub.
 *
 * Deezer is intentionally disabled in the privacy-hardened build. The type is
 * retained because the migration DI graph still expects DeezerSource; keeping
 * this small no-network implementation avoids widening the application's
 * runtime attack surface or contacting api.deezer.com.
 */
class DeezerSource(@Suppress("UNUSED_PARAMETER") private val http: HttpJson) : PlaylistSource {
    override val type = SourceType.DEEZER
    override val displayName = "Deezer (disabled)"

    override fun accepts(input: String): Boolean = false

    /** Compatibility helpers for the existing migration UI; neither performs network I/O. */
    fun isProfile(input: String): Boolean = false

    fun parseId(input: String): String =
        throw SourceError.Unsupported("Deezer is disabled in this privacy build")

    override suspend fun listPlaylists(input: String?): List<SourcePlaylist> =
        throw SourceError.Unsupported("Deezer is disabled in this privacy build")

    override suspend fun fetchTracks(playlistId: String): List<SourceTrack> =
        throw SourceError.Unsupported("Deezer is disabled in this privacy build")

    override suspend fun fetchArtists(collectionId: String): List<SourceArtist> =
        emptyList()
}
