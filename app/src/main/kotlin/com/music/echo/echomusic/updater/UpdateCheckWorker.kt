package iad1tya.echo.music.echomusic.updater

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Compatibility worker retained for the existing WorkManager wiring.
 * The privacy build performs no background update checks and never contacts GitHub.
 */
class UpdateCheckWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = Result.success()

    companion object {
        private const val WORK_NAME = "update_check_weekly"

        /** Deliberately a no-op: automatic update checks are disabled in the privacy build. */
        fun schedule(context: Context) = Unit
    }
}
