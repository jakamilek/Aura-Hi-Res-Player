plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.music.innertube"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

// Privacy-fork hardening: compile only the Room-compatible data models.
// The legacy YouTube/InnerTube client, parsers and network utilities remain
// in the repository for source-history compatibility but are not packaged.
sourceSets["main"].java.setSrcDirs(
    listOf("src/main/kotlin/com/music/innertube/models")
)

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Serialization is retained for the local response/data models only.
    // No HTTP client, OkHttp, Brotli or NewPipe network stack is included.
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    testImplementation(libs.junit)

    coreLibraryDesugaring(libs.desugaring)
}
