package iad1tya.echo.music.api

object AiPlaylistService {
    class UnsupportedProviderException(val providerName: String) :
        Exception("Provider not supported: $providerName")

    class MissingApiKeyException : Exception("AI API key is not set")

    class AiServiceUnavailableException(cause: Throwable? = null) :
        Exception("AI playlist service disabled", cause)

    suspend fun generate(
        prompt: String,
        count: Int,
        provider: String,
        apiKey: String,
        baseUrl: String,
        model: String,
        maxRetries: Int = 3,
    ): Result<AiPlaylistSpec> = Result.failure(AiServiceUnavailableException())

    suspend fun modify(
        currentTracks: List<TrackQuery>,
        prompt: String,
        provider: String,
        apiKey: String,
        baseUrl: String,
        model: String,
        maxRetries: Int = 3,
    ): Result<AiPlaylistEdit> = Result.failure(AiServiceUnavailableException())
}
