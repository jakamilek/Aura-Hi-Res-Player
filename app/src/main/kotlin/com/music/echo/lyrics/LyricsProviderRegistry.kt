package iad1tya.echo.music.lyrics

import iad1tya.echo.music.constants.PreferredLyricsProvider

object LyricsProviderRegistry {
    private val providerMap = mapOf(
        "BetterLyrics" to BetterLyricsProvider,
        "SimpMusic" to SimpMusicLyricsProvider,
        "LrcLib" to LrcLibLyricsProvider,
        "Kugou" to KuGouLyricsProvider,
    )

    val providerNames = providerMap.keys.toList()

    fun getProviderByName(name: String): LyricsProvider? = providerMap[name]

    fun deserializeProviderOrder(orderString: String): List<String> {
        if (orderString.isBlank()) return getDefaultProviderOrder()
        return orderString.split(",").map { it.trim() }.filter { it in providerNames }
    }

    fun serializeProviderOrder(providers: List<String>): String =
        providers.filter { it in providerNames }.joinToString(",")

    fun getDefaultProviderOrder(): List<String> = listOf(
        "LrcLib",
        "BetterLyrics",
        "SimpMusic",
        "Kugou",
    )

    fun getOrderedProviders(orderString: String): List<LyricsProvider> =
        deserializeProviderOrder(orderString).mapNotNull { getProviderByName(it) }

    fun getProviderNameForEnum(enum: PreferredLyricsProvider): String = when (enum) {
        PreferredLyricsProvider.LRCLIB        -> "LrcLib"
        PreferredLyricsProvider.KUGOU         -> "Kugou"
        PreferredLyricsProvider.BETTER_LYRICS -> "BetterLyrics"
        PreferredLyricsProvider.SIMPMUSIC     -> "SimpMusic"
        PreferredLyricsProvider.YOULYPLUS     -> "BetterLyrics"
        PreferredLyricsProvider.PAXSENIX      -> "BetterLyrics"
    }

    fun getDisplayName(name: String): String = when (name) {
        "BetterLyrics"    -> "Better Lyrics"
        "SimpMusic"       -> "SimpMusic"
        "LrcLib"          -> "LrcLib"
        "Kugou"           -> "KuGou"
        else              -> name
    }
}
