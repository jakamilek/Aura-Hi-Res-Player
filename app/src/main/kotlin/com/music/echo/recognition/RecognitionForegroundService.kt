package iad1tya.echo.music.recognition

/**
 * Compatibility-only holder for legacy notification/deep-link extra names.
 * Recognition itself has been removed from the privacy fork; this class performs no I/O,
 * starts no service, and requests no microphone permission.
 */
object RecognitionForegroundService {
    const val EXTRA_RECOGNITION_TRACK_ID = "recognition_track_id"
    const val EXTRA_RECOGNITION_TITLE = "recognition_title"
    const val EXTRA_RECOGNITION_ARTIST = "recognition_artist"
}
