package iad1tya.echo.music.echomusic.updater

import android.content.Context
import android.content.pm.PackageManager
import java.security.MessageDigest

/**
 * Read-only helper used by the Superpowered licence binding code.
 *
 * This class deliberately does not perform any network access and contains no APK
 * installation/update logic. It only returns SHA-256 digests of the certificates
 * that signed this installed package.
 */
object ApkSignatureVerifier {
    fun installedSignatureHashes(context: Context): List<String> {
        val pm = context.packageManager
        val packageName = context.packageName
        val signatures = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val signingInfo = pm.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES,
            ).signingInfo
            if (signingInfo.hasMultipleSigners()) {
                signingInfo.apkContentsSigners.toList()
            } else {
                signingInfo.signingCertificateHistory.toList()
            }
        } else {
            @Suppress("DEPRECATION")
            pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures.toList()
        }

        return signatures
            .map { signature ->
                MessageDigest.getInstance("SHA-256")
                    .digest(signature.toByteArray())
                    .joinToString("") { byte -> "%02x".format(byte) }
            }
            .distinct()
            .sorted()
    }
}
