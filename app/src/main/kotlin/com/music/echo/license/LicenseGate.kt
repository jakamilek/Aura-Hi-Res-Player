package iad1tya.echo.music.license

import androidx.compose.runtime.Composable

/**
 * Privacy fork: subscription licensing is removed.
 * Qobuz authentication and entitlement are handled directly by Qobuz.
 */
@Composable
fun LicenseGate(appContent: @Composable () -> Unit) {
    appContent()
}
