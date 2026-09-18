package iad1tya.echo.music.utils.cipher

import android.content.Context

/**
 * Privacy-safe compatibility shim for the former remote player-config updater.
 *
 * Player cipher configurations are intentionally shipped only from the audited,
 * in-app hardcoded table in [FunctionNameExtractor]. This class remains as a
 * no-network API shim so older call sites stay source-compatible, but it never
 * contacts a remote server, reads a remote cache, or applies remotely supplied
 * JavaScript/configuration.
 */
object RemotePlayerConfig {
    @Volatile
    var configEpoch: Int = 0
        private set

    fun configFor(hash: String): FunctionNameExtractor.HardcodedPlayerConfig? = null

    fun loadCache(context: Context) = Unit

    suspend fun refresh(context: Context) = Unit

    suspend fun forceRefresh(context: Context, missingHash: String): Boolean = false

    suspend fun manualRefresh(context: Context): Boolean = false

    fun lastRefreshTimeMs(): Long = 0L

    fun knownHashCount(): Int = 0
}
