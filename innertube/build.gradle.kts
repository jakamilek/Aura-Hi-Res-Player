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

    sourceSets {
        getByName("main") {
            // Keep only local response/data types required by the existing app database/UI
            // compatibility layer. Do NOT compile the legacy network/client utilities.
            java.setSrcDirs(
                listOf(
                    "src/main/kotlin/com/music/innertube/models",
                    "src/main/kotlin/com/music/innertube/pages",
                )
            )
        }
    }
}

// Privacy-fork hardening: InnerTube is data-model/page-model compatibility only.
// The legacy YouTube/InnerTube client and network utilities are deliberately excluded.

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
    testImplementation(libs.junit)
    coreLibraryDesugaring(libs.desugaring)
}
