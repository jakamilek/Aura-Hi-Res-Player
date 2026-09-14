package iad1tya.echo.music.echomusic.updater.downloadmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Compatibility stub for the removed in-app updater.
 * No update URL is accepted or fetched in the privacy build.
 */
class UpdateDownloadWorker(
    private val context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = Result.failure()

    companion object {
        const val WORK_NAME = "update_download"
        const val KEY_APK_URL = "apk_url"
        const val KEY_VERSION = "version"
        const val KEY_FILE_SIZE = "file_size"
        const val KEY_FILE_SIZE_BYTES = "file_size_bytes"
    }
}
