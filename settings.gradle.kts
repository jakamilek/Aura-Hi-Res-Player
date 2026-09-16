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
include(":applecanvas")
include(":echomusiccanvas")
include(":unison")

// YouTube/InnerTube and third-party lyrics/proxy modules are intentionally
// not included in the privacy fork.
