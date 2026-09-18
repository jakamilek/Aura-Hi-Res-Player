package iad1tya.echo.music.api

object OpenRouterService {
    suspend fun translate(
        text: String,
        targetLanguage: String,
        apiKey: String,
        baseUrl: String,
        model: String,
        mode: String,
        maxRetries: Int = 3,
        sourceLanguage: String? = null,
    ): Result<List<String>> = Result.failure(UnsupportedOperationException("Remote translation disabled"))
}
