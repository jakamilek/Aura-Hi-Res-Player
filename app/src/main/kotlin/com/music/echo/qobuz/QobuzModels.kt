package iad1tya.echo.music.qobuz

import androidx.annotation.StringRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Data models for the official Qobuz API. */

// ── Catalog/search ───────────────────────────────────────────────────────────

@Serializable
data class QobuzSearchData(
    val tracks: QobuzTrackPage? = null,
)

@Serializable
data class QobuzTrackPage(
    val items: List<QobuzTrack> = emptyList(),
)

@Serializable
data class QobuzTrack(
    val id: Long = 0L,
    val title: String = "",
    val duration: Int = 0,
    val streamable: Boolean = false,
    @SerialName("maximum_bit_depth") val maximumBitDepth: Int = 0,
    @SerialName("maximum_sampling_rate") val maximumSamplingRate: Double = 0.0,
    val performer: QobuzPerformer? = null,
)

@Serializable
data class QobuzPerformer(
    val id: Long? = null,
    val name: String? = null,
)

// ── Auth ─────────────────────────────────────────────────────────────────────

@Serializable
data class QobuzLoginResponse(
    @SerialName("user_auth_token") val userAuthToken: String? = null,
    val user: QobuzUser? = null,
)

@Serializable
data class QobuzUser(
    val id: Long? = null,
    val email: String? = null,
    val login: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val credential: QobuzCredential? = null,
)

@Serializable
data class QobuzCredential(
    val id: Long? = null,
    val label: String? = null,
    val description: String? = null,
    val parameters: QobuzCredentialParameters? = null,
)

@Serializable
data class QobuzCredentialParameters(
    val label: String? = null,
    @SerialName("short_label") val shortLabel: String? = null,
    @SerialName("lossy_streaming") val lossyStreaming: Boolean = false,
    @SerialName("lossless_streaming") val losslessStreaming: Boolean = false,
    @SerialName("hires_streaming") val hiresStreaming: Boolean = false,
    @SerialName("hires_purchases_streaming") val hiresPurchasesStreaming: Boolean = false,
)

// ── Signed stream URL ────────────────────────────────────────────────────────

@Serializable
data class QobuzFileUrlResponse(
    @SerialName("track_id") val trackId: Long? = null,
    val url: String? = null,
    @SerialName("format_id") val formatId: Int = 0,
    @SerialName("mime_type") val mimeType: String? = null,
    @SerialName("sampling_rate") val samplingRate: Double? = null,
    @SerialName("bit_depth") val bitDepth: Int? = null,
    val duration: Int = 0,
    val sample: Boolean = false,
    val restrictions: List<QobuzRestriction> = emptyList(),
) {
    val isPlayableStream: Boolean
        get() = !sample && !url.isNullOrBlank() && (samplingRate ?: 0.0) > 0.0

    val isLosslessDelivery: Boolean
        get() = isPlayableStream &&
            formatId in QobuzQualityFormat.LOSSLESS_IDS &&
            mimeType?.contains("flac", ignoreCase = true) == true &&
            (bitDepth ?: 0) >= 16 &&
            (samplingRate ?: 0.0) > 0.0
}

@Serializable
data class QobuzRestriction(
    val code: String? = null,
)

// ── Domain results ──────────────────────────────────────────────────────────

data class QobuzStream(
    val url: String,
    val formatId: Int,
    val bitDepth: Int,
    val samplingRateHz: Int,
    val mimeType: String,
    val bitrateBps: Int,
)

data class QobuzSession(
    val token: String,
    val appId: String,
    val appSecret: String,
    val email: String? = null,
    val tierLabel: String? = null,
    val hiresEntitled: Boolean = false,
)

sealed interface QobuzLoginResult {
    data class Success(
        val session: QobuzSession,
        val freeOrLossyOnly: Boolean,
    ) : QobuzLoginResult

    data class Error(@StringRes val messageRes: Int) : QobuzLoginResult
}
