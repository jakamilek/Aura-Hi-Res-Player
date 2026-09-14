package iad1tya.echo.music.echomusic.updater

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import iad1tya.echo.music.BuildConfig

/**
 * Privacy-build update compatibility layer.
 *
 * The original updater contacted the upstream hck0n3 GitHub repository and could download and
 * install an APK from that repository. This fork intentionally has no in-app update network path.
 * Releases are installed manually after being audited and signed with the release key.
 */

data class ChangelogSection(val title: String, val items: List<String>)

sealed class EchoUpdateStatus {
    object Idle : EchoUpdateStatus()
    object Checking : EchoUpdateStatus()
    data class Available(
        val version: String,
        val changelog: List<ChangelogSection> = emptyList(),
        val size: String = "",
        val sizeBytes: Long = 0L,
        val releaseDate: String = "",
        val description: String? = null,
        val imageUrl: String? = null,
        val apkUrl: String? = null,
    ) : EchoUpdateStatus()
    data class NoUpdate(val version: String) : EchoUpdateStatus()
    data class Error(val message: String) : EchoUpdateStatus()
}

/** Extract HTTP(S) URLs from changelog text for the existing link-rendering UI. */
fun String.extractUrls(): List<Pair<IntRange, String>> {
    val regex = Regex("https?://[^\\s<>\\\"')]+")
    return regex.findAll(this).map { match ->
        match.range to match.value.trimEnd('.', ',', ';', ':', '!', '?', ')', ']', '}')
    }.filter { (range, url) -> url.isNotBlank() && range.first < range.last + 1 }.toList()
}

@Composable
fun UpdateScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Aktualizacje wyłączone w buildzie privacy",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "Wersja ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )
        Text(
            text = "Aplikacja nie kontaktuje się automatycznie z GitHubem i nie pobiera APK z zewnętrznego repozytorium.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 16.dp),
        )
        Button(
            onClick = { navController.navigateUp() },
            modifier = Modifier.padding(top = 24.dp),
        ) {
            Text("Wróć")
        }
    }
}

const val PREFS_NAME = "settings"
const val KEY_AUTO_UPDATE_CHECK = "auto_update_check"
const val KEY_LAST_CHECKED_TIME = "last_checked_time"
const val KEY_BETA_UPDATES = "beta_updates"
const val KEY_UPDATE_AVAILABLE = "update_available"
const val KEY_UPDATE_NOTIFICATIONS = "update_notifications"

fun getUpdateAvailableState(context: Context): Boolean =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_UPDATE_AVAILABLE, false)

fun saveUpdateAvailableState(context: Context, available: Boolean) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(KEY_UPDATE_AVAILABLE, available).apply()
}

fun getAutoUpdateCheckSetting(context: Context): Boolean =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_AUTO_UPDATE_CHECK, false)

fun saveAutoUpdateCheckSetting(context: Context, enabled: Boolean) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(KEY_AUTO_UPDATE_CHECK, false).apply()
}

fun getUpdateNotificationsSetting(context: Context): Boolean =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getBoolean(KEY_UPDATE_NOTIFICATIONS, false)

fun saveUpdateNotificationsSetting(context: Context, enabled: Boolean) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit().putBoolean(KEY_UPDATE_NOTIFICATIONS, false).apply()
}

fun saveLastCheckedTime(context: Context, timestamp: String) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit().putString(KEY_LAST_CHECKED_TIME, timestamp).apply()
}

fun getLastCheckedTime(context: Context): String =
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getString(KEY_LAST_CHECKED_TIME, "") ?: ""

fun getBetaUpdatesSetting(context: Context): Boolean = false

fun saveBetaUpdatesSetting(context: Context, enabled: Boolean) = Unit

fun isNewerVersion(latestVersion: String, currentVersion: String): Boolean = false

suspend fun checkForUpdate(
    context: Context,
    onSuccess: (tag: String, isAvailable: Boolean, changelog: List<ChangelogSection>, size: String, sizeBytes: Long, date: String, description: String?, imageUrl: String?, apkUrl: String?) -> Unit,
    onError: () -> Unit,
) {
    // Deliberately no network I/O. Keep the callback contract for existing callers/workers.
    saveUpdateAvailableState(context, false)
    onSuccess(BuildConfig.VERSION_NAME, false, emptyList(), "", 0L, "", null, null, null)
}
