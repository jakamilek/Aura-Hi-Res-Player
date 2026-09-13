package iad1tya.echo.music.recognition

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.music.shazamkit.models.RecognitionStatus

/** Privacy-fork compatibility layer. Song recognition is disabled and performs no I/O. */
object RemoteRecognitionConfig {
    fun loadCache(@Suppress("UNUSED_PARAMETER") context: android.content.Context) = Unit
    suspend fun refresh(@Suppress("UNUSED_PARAMETER") context: android.content.Context) = Unit
}

/** Compatibility service retained only for old intent references; it never starts recognition. */
class RecognitionForegroundService : Service() {
    companion object {
        const val EXTRA_RECOGNITION_TRACK_ID = "extra_recognition_track_id"
        const val EXTRA_RECOGNITION_TITLE = "extra_recognition_title"
        const val EXTRA_RECOGNITION_ARTIST = "extra_recognition_artist"
    }
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_NOT_STICKY
}

/** No-op compatibility API for legacy callers. No microphone, network or persistence access. */
object MusicRecognitionService {
    private val _recognitionStatus = MutableStateFlow<RecognitionStatus>(RecognitionStatus.Ready)
    val recognitionStatus: StateFlow<RecognitionStatus> = _recognitionStatus
    fun isInProgress(): Boolean = false
    fun reset() { _recognitionStatus.value = RecognitionStatus.Ready }
    fun cancel(@Suppress("UNUSED_PARAMETER") context: android.content.Context) { reset() }
}
