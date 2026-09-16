@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

// F-Droid doesn't support foojay-resolver plugin
// plugins {
//     id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
// }

rootProject.name = "echomusic"
include(":app")
include(":migration")
include(":canvas")
include(":kugou")
include(":innertube")
include(":applecanvas")
include(":echomusiccanvas")
include(":unison")

// The InnerTube module remains because the existing app database, models and
// compatibility layer still depend on its data types. YouTube-only runtime
// clients/endpoints are removed separately; this module must not be removed
// wholesale until all model consumers have been migrated.
// Third-party lyrics/proxy modules remain excluded from the privacy build.
