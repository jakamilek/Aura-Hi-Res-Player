package iad1tya.echo.music.lyrics

import android.content.Context
import iad1tya.echo.music.db.MusicDatabase
import iad1tya.echo.music.db.entities.LyricsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/** Privacy compatibility layer: lyric translation providers are removed and never contacted. */
object LyricsTranslationHelper {
    enum class TranslationStatus { Idle, Translating, Success, Error }

    private val _status = MutableStateFlow<TranslationStatus>(TranslationStatus.Idle)
    val status: StateFlow<TranslationStatus> = _status

    private val _hasActiveTranslations = MutableStateFlow(false)
    val hasActiveTranslations: StateFlow<Boolean> = _hasActiveTranslations

    private val _manualTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val manualTranslationTrigger: SharedFlow<Unit> = _manualTrigger
    val manualTrigger: SharedFlow<Unit> = _manualTrigger

    private val _clearTranslationsTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val clearTranslationsTrigger: SharedFlow<Unit> = _clearTranslationsTrigger

    fun triggerManualTranslation() { _manualTrigger.tryEmit(Unit) }
    fun triggerClearTranslations() {
        _hasActiveTranslations.value = false
        _clearTranslationsTrigger.tryEmit(Unit)
    }

    fun hasTranslations(lyricsEntity: LyricsEntity?): Boolean = !lyricsEntity?.translatedLyrics.isNullOrBlank()

    fun clearTranslations(lyricsEntity: LyricsEntity): LyricsEntity = lyricsEntity.copy(
        translatedLyrics = "",
        translationLanguage = "",
        translationMode = "",
    )

    fun resetStatus() { _status.value = TranslationStatus.Idle }
    fun clearCache() = Unit
    fun setCompositionActive(active: Boolean) = Unit
    fun cancelTranslation() { _status.value = TranslationStatus.Idle }

    fun getCachedTranslations(lyrics: List<LyricsEntry>, mode: String, language: String): List<String>? = null
    fun applyCachedTranslations(lyrics: List<LyricsEntry>, mode: String, language: String): Boolean = false

    fun loadTranslationsFromDatabase(
        lyrics: List<LyricsEntry>,
        lyricsEntity: LyricsEntity?,
        targetLanguage: String,
        mode: String,
    ) {
        lyrics.forEach { it.translatedTextFlow.value = null }
        if (lyricsEntity?.translatedLyrics.isNullOrBlank() ||
            lyricsEntity.translationLanguage != targetLanguage ||
            lyricsEntity.translationMode != mode
        ) {
            _hasActiveTranslations.value = false
            return
        }
        val translatedLines = lyricsEntity.translatedLyrics.lines()
        lyrics.mapIndexedNotNull { index, entry -> if (entry.text.isNotBlank()) index to entry else null }
            .forEachIndexed { idx, (originalIndex, _) ->
                if (idx < translatedLines.size) lyrics[originalIndex].translatedTextFlow.value = translatedLines[idx]
            }
        _hasActiveTranslations.value = true
    }

    fun translateLyrics(
        lyrics: List<LyricsEntry>,
        targetLanguage: String,
        apiKey: String,
        baseUrl: String,
        model: String,
        mode: String,
        scope: CoroutineScope,
        context: Context,
        provider: String = "OpenRouter",
        deeplApiKey: String = "",
        deeplFormality: String = "default",
        useStreaming: Boolean = true,
        songId: String = "",
        database: MusicDatabase? = null,
        keyless: Boolean = false,
    ) {
        _status.value = TranslationStatus.Idle
        lyrics.forEach { it.translatedTextFlow.value = null }
        _hasActiveTranslations.value = false
    }
}
