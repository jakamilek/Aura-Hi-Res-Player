package iad1tya.echo.music.api

object DeepLService {
    suspend fun translate(
        text: String,
        targetLanguage: String,
        apiKey: String,
        formality: String = "default",
        maxRetries: Int = 3,
    ): Result<List<String>> = Result.failure(UnsupportedOperationException("Remote translation disabled"))
}
