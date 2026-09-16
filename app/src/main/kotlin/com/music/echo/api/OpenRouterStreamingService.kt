package iad1tya.echo.music.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object OpenRouterStreamingService {
    sealed class StreamChunk {
        data class Content(val text: String) : StreamChunk()
        data class Complete(val translatedLines: List<String>) : StreamChunk()
        data class Error(val message: String) : StreamChunk()
    }

    fun streamTranslation(
        text: String,
        targetLanguage: String,
        apiKey: String,
        baseUrl: String,
        model: String,
        mode: String,
    ): Flow<StreamChunk> = flow {
        emit(StreamChunk.Error("Remote translation disabled"))
    }
}
