package iad1tya.echo.music.recognition

import android.app.Service
import android.content.Intent
import android.os.IBinder

/** Privacy fork compatibility layer. Song recognition is disabled and performs no I/O. */
object RemoteRecognitionConfig {
    fun loadCache(@Suppress("UNUSED_PARAMETER") context: android.content.Context) = Unit
    suspend fun refresh(@Suppress("UNUSED_PARAMETER") context: android.content.Context) = Unit
}

class RecognitionForegroundService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_NOT_STICKY
}
