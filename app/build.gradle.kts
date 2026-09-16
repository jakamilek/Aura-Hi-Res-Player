import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.util.Base64
import java.util.Collections
import java.security.KeyStore
import java.security.MessageDigest
import java.net.URL

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) localProperties.load(localPropertiesFile.inputStream())
plugins {
    id("com.android.application")
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.protobufPlugin)
}
val hasGoogleServicesConfig = file("google-services.json").exists()
if (hasGoogleServicesConfig) { apply(plugin = "com.google.gms.google-services") }
val superpoweredLicenseKey: String = run {
    val raw = localProperties.getProperty("SUPERPOWERED_LICENSE_KEY")?.takeIf { it.isNotBlank() } ?: System.getenv("SUPERPOWERED_LICENSE_KEY")?.takeIf { it.isNotBlank() } ?: ""
    val trimmed = raw.trim()
    if (trimmed.isNotEmpty() && trimmed.any { it.code !in 0x21..0x7E || it == '"' || it == '\\' }) { logger.warn("SUPERPOWERED: invalid licence key characters; ignored."); "" } else trimmed
}
val releaseSigningCertSha256: String? = run {
    val fromKeystore = runCatching {
        val storeFile = file("keystore/release.keystore")
        if (!storeFile.exists()) return@runCatching null
        val storePassword = (System.getenv("STORE_PASSWORD") ?: localProperties.getProperty("STORE_PASSWORD"))?.takeIf { it.isNotBlank() } ?: return@runCatching null
        val preferredAlias = (System.getenv("KEY_ALIAS") ?: localProperties.getProperty("KEY_ALIAS"))?.takeIf { it.isNotBlank() }
        var digest: String? = null
        for (type in listOf("PKCS12", "JKS")) {
            digest = runCatching {
                val ks = KeyStore.getInstance(type); storeFile.inputStream().use { ks.load(it, storePassword.toCharArray()) }
                val aliases = Collections.list(ks.aliases()); val alias = preferredAlias?.takeIf { ks.containsAlias(it) && ks.getCertificate(it) != null } ?: aliases.firstOrNull { ks.getCertificate(it) != null } ?: return@runCatching null
                MessageDigest.getInstance("SHA-256").digest(ks.getCertificate(alias)?.encoded ?: return@runCatching null).joinToString("") { b -> "%02x".format(b) }
            }.getOrNull(); if (digest != null) break
        }; digest
    }.getOrNull()
    fromKeystore ?: (localProperties.getProperty("SUPERPOWERED_CERT_SHA256") ?: System.getenv("SUPERPOWERED_CERT_SHA256"))?.replace(":", "")?.replace(" ", "")?.trim()?.lowercase()?.takeIf { it.length == 64 && it.all { c -> c in "0123456789abcdef" } }
}
fun superpoweredBind(keyBytes: ByteArray, certDigest: ByteArray): ByteArray {
    val out = ByteArray(keyBytes.size); var offset = 0; var counter = 0
    while (offset < keyBytes.size) {
        val md = MessageDigest.getInstance("SHA-256"); md.update("aura-sp-v1".toByteArray(Charsets.UTF_8)); md.update(certDigest); md.update(byteArrayOf((counter ushr 24).toByte(), (counter ushr 16).toByte(), (counter ushr 8).toByte(), counter.toByte()))
        val block = md.digest(); var i = 0
        while (i < block.size && offset < keyBytes.size) { out[offset] = (keyBytes[offset].toInt() xor block[i].toInt()).toByte(); offset++; i++ }
        counter++
    }; return out
}
val superpoweredBoundBlob: String? = run {
    val certHex = releaseSigningCertSha256; val key = superpoweredLicenseKey
    if (key.isEmpty() || certHex == null) return@run null
    val certDigest = ByteArray(32) { i -> certHex.substring(i * 2, i * 2 + 2).toInt(16).toByte() }
    Base64.getEncoder().encodeToString(superpoweredBind(key.toByteArray(Charsets.UTF_8), certDigest))
}
val superpoweredKeyChecksum: String = if (superpoweredLicenseKey.isEmpty()) "" else MessageDigest.getInstance("SHA-256").digest(superpoweredLicenseKey.toByteArray(Charsets.UTF_8)).take(4).joinToString("") { b -> "%02x".format(b) }
run {
    val goldenCertHex = "00112233445566778899aabbccddeeff00112233445566778899aabbccddeeff"; val goldenKey = "AuraGoldenVectorKey-0123456789+/=abcdefgh"; val goldenBlob = "jtt6qa0FokrPxZizx+797Uuw9+VKqbM6zs3zxiEXCxIE7azZl951bZ8="; val goldenDigest = ByteArray(32) { i -> goldenCertHex.substring(i * 2, i * 2 + 2).toInt(16).toByte() }; val produced = Base64.getEncoder().encodeToString(superpoweredBind(goldenKey.toByteArray(Charsets.UTF_8), goldenDigest)); check(produced == goldenBlob) { "SUPERPOWERED: superpoweredBind golden vector mismatch." }
}
when { superpoweredLicenseKey.isEmpty() -> logger.warn("SUPERPOWERED: no licence key configured; DSP engine unavailable."); superpoweredBoundBlob == null -> logger.warn("SUPERPOWERED: release certificate unavailable; licence remains unbound.") }
android {
    namespace = "iad1tya.echo.music"; compileSdk = 36; ndkVersion = "27.0.12077973"
    externalNativeBuild { cmake { path("src/main/cpp/CMakeLists.txt"); version = "3.22.1+" } }
    val noSub = project.hasProperty("nosub") && project.property("nosub") == "true"
    defaultConfig {
        applicationId = if (noSub) "iad1tya.aura.music.dev" else "iad1tya.aura.music"; if (noSub) versionNameSuffix = "-nosub"; buildConfigField("Boolean", "REQUIRE_SUBSCRIPTION", (!noSub).toString()); minSdk = 26; targetSdk = 36; versionCode = 951; versionName = "0.6.231"; testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"; vectorDrawables.useSupportLibrary = true
        val tidalClientId = localProperties.getProperty("TIDAL_CLIENT_ID")?.takeIf { it.isNotBlank() } ?: System.getenv("TIDAL_CLIENT_ID")?.takeIf { it.isNotBlank() } ?: "nNgez049R98ji742"; buildConfigField("String", "TIDAL_CLIENT_ID", "\"$tidalClientId\""); val qobuzAppId = localProperties.getProperty("QOBUZ_APP_ID")?.takeIf { it.isNotBlank() } ?: System.getenv("QOBUZ_APP_ID")?.takeIf { it.isNotBlank() } ?: ""; val qobuzAppSecret = localProperties.getProperty("QOBUZ_APP_SECRET")?.takeIf { it.isNotBlank() } ?: System.getenv("QOBUZ_APP_SECRET")?.takeIf { it.isNotBlank() } ?: ""; buildConfigField("String", "QOBUZ_APP_ID", "\"$qobuzAppId\""); buildConfigField("String", "QOBUZ_APP_SECRET", "\"$qobuzAppSecret\"")
        buildConfigField("String", "LASTFM_API_KEY", "\""); buildConfigField("String", "LASTFM_SECRET", "\""); buildConfigField("String", "SUPERPOWERED_LICENSE", "\"$superpoweredLicenseKey\""); buildConfigField("boolean", "SUPERPOWERED_LICENSE_BOUND", "false"); buildConfigField("String", "SUPERPOWERED_LICENSE_CHECK", "\"$superpoweredKeyChecksum\""); val isNightly = project.hasProperty("nightly") && project.property("nightly") == "true"; buildConfigField("Boolean", "IS_NIGHTLY", isNightly.toString()); externalNativeBuild { cmake { cppFlags("-std=c++17", "-fexceptions", "-frtti"); arguments("-DANDROID_STL=c++_shared") } }
    }
    flavorDimensions += listOf("abi", "variant")
    productFlavors { create("foss") { dimension = "variant"; isDefault = true; buildConfigField("Boolean", "CAST_AVAILABLE", "false") }; create("gms") { dimension = "variant"; buildConfigField("Boolean", "CAST_AVAILABLE", "true") }; create("universal") { dimension = "abi"; buildConfigField("String", "ARCHITECTURE", "\"universal\"") }; create("arm64") { dimension = "abi"; ndk { abiFilters += "arm64-v8a" }; buildConfigField("String", "ARCHITECTURE", "\"arm64\"") }; create("armeabi") { dimension = "abi"; ndk { abiFilters += "armeabi-v7a" }; buildConfigField("String", "ARCHITECTURE", "\"armeabi\"") }; create("x86") { dimension = "abi"; buildConfigField("String", "ARCHITECTURE", "\"x86\"") }; create("x86_64") { dimension = "abi"; buildConfigField("String", "ARCHITECTURE", "\"x86_64\"") } }
    signingConfigs { create("persistentDebug") { storeFile = file("persistent-debug.keystore"); storePassword = "android"; keyAlias = "androiddebugkey"; keyPassword = "android" }; create("release") { storeFile = file("keystore/release.keystore"); storePassword = (System.getenv("STORE_PASSWORD") ?: localProperties.getProperty("STORE_PASSWORD"))?.takeIf { it.isNotBlank() }; keyAlias = (System.getenv("KEY_ALIAS") ?: localProperties.getProperty("KEY_ALIAS"))?.takeIf { it.isNotBlank() }; keyPassword = (System.getenv("KEY_PASSWORD") ?: localProperties.getProperty("KEY_PASSWORD"))?.takeIf { it.isNotBlank() }; }; getByName("debug") { keyAlias = "androiddebugkey"; keyPassword = "android"; storePassword = "android"; storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore") } }
    buildTypes { release { isMinifyEnabled = true; isShrinkResources = true; isCrunchPngs = false; isDebuggable = false; signingConfig = signingConfigs.getByName("debug"); proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"); buildConfigField("String", "ARCHITECTURE", "\"release\""); superpoweredBoundBlob?.let { blob -> buildConfigField("String", "SUPERPOWERED_LICENSE", "\"$blob\""); buildConfigField("boolean", "SUPERPOWERED_LICENSE_BOUND", "true") } }; debug { applicationIdSuffix = ".debug"; isDebuggable = true; signingConfig = signingConfigs.getByName("debug"); buildConfigField("String", "ARCHITECTURE", "\"debug\"") } }
    compileOptions { isCoreLibraryDesugaringEnabled = true; sourceCompatibility = JavaVersion.VERSION_21; targetCompatibility = JavaVersion.VERSION_21 }; kotlin { jvmToolchain(21); compilerOptions { freeCompilerArgs.add("-Xannotation-default-target=param-property"); jvmTarget.set(JvmTarget.JVM_21) } }; buildFeatures { compose = true; buildConfig = true }; dependenciesInfo { includeInApk = false; includeInBundle = false }; lint { lintConfig = file("lint.xml"); warningsAsErrors = false; abortOnError = false; checkDependencies = false; checkReleaseBuilds = false }; androidResources { generateLocaleConfig = true }; packaging { jniLibs { useLegacyPackaging = true; keepDebugSymbols += listOf("**/libandroidx.graphics.path.so", "**/libdatastore_shared_counter.so") }; resources { excludes += "/META-INF/{AL2.0,LGPL2.1}"; excludes += "META-INF/NOTICE.md"; excludes += "META-INF/CONTRIBUTORS.md"; excludes += "META-INF/LICENSE.md"; excludes += "META-INF/INDEX.LIST"; excludes += "META-INF/DEPENDENCIES" } }
}
protobuf { protoc { artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}" }; generateProtoTasks { all().forEach { task -> task.builtins { create("java") { option("lite") }; create("kotlin") { option("lite") } } } } }
ksp { arg("room.schemaLocation", "$projectDir/schemas") }
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach { compilerOptions { freeCompilerArgs.addAll("-opt-in=kotlin.RequiresOptIn"); suppressWarnings.set(false) } }
dependencies {
    testImplementation(libs.junit)
    implementation(libs.haze); implementation(libs.guava); implementation(libs.coroutines.guava); implementation(libs.concurrent.futures); implementation(libs.activity); implementation(libs.hilt.navigation); implementation(libs.datastore); implementation(libs.compose.runtime); implementation(libs.compose.foundation); implementation(libs.compose.ui); implementation(libs.compose.ui.util); implementation(libs.compose.ui.tooling); implementation(libs.compose.animation); implementation(libs.compose.reorderable); implementation(libs.viewmodel); implementation(libs.viewmodel.compose); implementation(libs.material3); implementation(libs.androidx.adaptive); implementation(libs.androidx.adaptive.layout); implementation(libs.androidx.adaptive.navigation); implementation(libs.palette); implementation(libs.materialKolor); implementation(libs.appcompat); implementation(libs.coil); implementation(libs.coil.network.okhttp); implementation(libs.ucrop); implementation(libs.shimmer); implementation(libs.media3); implementation(libs.media3.session); implementation(libs.media3.hls); implementation(libs.media3.ui); implementation(libs.media3.okhttp)
    "gmsImplementation"(libs.media3.cast); "gmsImplementation"(libs.mediarouter); "gmsImplementation"(libs.cast.framework); implementation(libs.room.runtime); implementation(libs.kuromoji.ipadic); implementation("dev.ffmpegkit-maintained:ffmpeg-kit-full:8.1.7"); ksp(libs.room.compiler); implementation(libs.room.ktx); implementation(libs.apache.lang3); implementation(libs.hilt); implementation(libs.jsoup); ksp(libs.hilt.compiler)
    implementation(project(":innertube")); implementation(project(":migration")); implementation("androidx.security:security-crypto:1.1.0-alpha06"); implementation(project(":kugou")); implementation(project(":lrclib")); implementation(project(":betterlyrics")); implementation(project(":simpmusic")); implementation(project(":youlyplus")); implementation(project(":canvas")); implementation(project(":artistvideo")); implementation(project(":applecanvas")); implementation(project(":echomusiccanvas")); implementation(project(":paxsenixlyrics")); implementation(project(":unison")); implementation(libs.ktor.client.core); implementation(libs.ktor.client.okhttp); implementation(libs.ktor.client.content.negotiation); implementation(libs.ktor.serialization.json); implementation(libs.protobuf.javalite); implementation(libs.protobuf.kotlin.lite); coreLibraryDesugaring(libs.desugaring); implementation(libs.timber); implementation("com.jakewharton:process-phoenix:3.0.0"); implementation("androidx.browser:browser:1.8.0"); implementation(libs.smoothCorner); implementation(libs.lottie.compose); implementation("androidx.compose.material:material-icons-extended:1.7.8"); implementation(libs.work.runtime.ktx); implementation(libs.androidx.core.splashscreen)
}
