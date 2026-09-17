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

    // Privacy-fork hardening: only the data-model package is compiled.
    // The legacy YouTube/InnerTube pages and utility code remain in the tree
    // only for source-history compatibility and are deliberately excluded from
    // the Android library. This keeps Room-compatible model types available
    // without shipping a YouTube runtime/client surface.
    sourceSets["main"].java.setSrcDirs(
        listOf("src/main/kotlin/com/music/innertube/models")
    )

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    // Serialization is required by the retained local response/data models.
    // No HTTP client, OkHttp, Brotli or NewPipe network stack is included.
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    testImplementation(libs.junit)

    coreLibraryDesugaring(libs.desugaring)
}
