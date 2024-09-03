@file:Suppress("ConstPropertyName")

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object Versions {
  const val minSdk = 23
  const val targetSdk = 34
  const val compileSdk = 34

  private const val majorVersion = 0
  private const val minorVersion = 1
  private const val patchVersion = 1
  const val versionName = "$majorVersion.$minorVersion.$patchVersion"
  const val versionCode = 2402

  val javaVersion = JavaVersion.VERSION_1_8
  val jvmTarget = JvmTarget.JVM_1_8
}

// packaging-resources-excludes
object Resources {
  val excludes = listOf(
    "**/kotlin-tooling-metadata.json",
    "**.properties",
    "**.bin",
    "**/*.proto",
    // https://github.com/Kotlin/kotlinx.coroutines/issues/2023
    "META-INF/**",
    "**/attach_hotspot_windows.dll",
    // ktor
    // "**/custom.config.conf",
    // "**/custom.config.yaml",
    // assets/sentry-external-modules.txt
    "**/sentry-external-modules.txt",
  )
}
