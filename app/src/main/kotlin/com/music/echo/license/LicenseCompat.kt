package iad1tya.echo.music.license

import android.content.Context
import androidx.compose.runtime.Composable

/**
 * Privacy-fork compatibility surface.
 * Subscription licensing and the associated remote backend were removed from this build.
 * These tiny no-op definitions keep older settings screens source-compatible until those screens
 * are simplified further; they perform no I/O, device-ID lookup, or network access.
 */
object LicenseLogic {
    enum class AppState { SUBSCRIPTION_ACTIVE, SUBSCRIPTION_REQUIRED }
}

object LicenseManager {
    fun lastResolvedState(@Suppress("UNUSED_PARAMETER") context: Context): LicenseLogic.AppState =
        LicenseLogic.AppState.SUBSCRIPTION_ACTIVE
}

@Composable
fun SubscriptionEntryScreen(
    onActivated: () -> Unit,
    onBack: () -> Unit,
) {
    // Licensing is intentionally absent in the privacy fork. The screen should never be reachable
    // because REQUIRE_SUBSCRIPTION is false; callbacks are retained only for source compatibility.
    @Suppress("UNUSED_VARIABLE")
    val unused = onActivated to onBack
}
