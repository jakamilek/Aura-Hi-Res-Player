package iad1tya.echo.music.utils

/**
 * GMS build: no-op crash reporter.
 *
 * Firebase Crashlytics is intentionally removed from the privacy fork.
 * Keep the same public API as the FOSS implementation so shared code
 * can compile without telemetry dependencies.
 */
object CrashReporter {
    fun record(throwable: Throwable) {
        // Intentionally empty.
    }

    fun log(message: String) {
        // Intentionally empty.
    }

    fun setKey(key: String, value: String) {
        // Intentionally empty.
    }
}
