import org.gradle.api.JavaVersion

object Versions {
  const val minSdk = 23
  const val targetSdk = 33
  const val compileSdk = 34

  private const val majorVersion = 0
  private const val minorVersion = 1
  private const val patchVersion = 0
  const val versionName = "$majorVersion.$minorVersion.$patchVersion"
  const val versionCode = 2401

  // https://developer.android.com/jetpack/androidx/releases/compose-compiler?hl=zh-cn
  const val composeCompiler = "1.5.14"

  // jvmToolchain
  const val jdkVersion = 17

  // starting with AGP 7.4.0 we need to target JVM 11 bytecode
  val java = JavaVersion.VERSION_11
}

// packaging-resources-excludes
object Resources {
  val excludes = listOf(
    "DebugProbesKt.bin",
    "kotlin-tooling-metadata.json",
    "kotlin/**",
    // Only exclude *.version files in release mode as debug mode requires these files for layout inspector to work.
    // "META-INF/*.version",
    "META-INF/{AL2.0,LGPL2.1}",
    "META-INF/DEPENDENCIES",
    "META-INF/DEPENDENCIES.txt",
    "META-INF/NOTICE",
    "META-INF/NOTICE.txt",
    "META-INF/LICENSE",
    "META-INF/LICENSE.txt",
    "META-INF/INDEX.LIST",
    "META-INF/*.kotlin_module",
    // assets/sentry-external-modules.txt
    "**/sentry-external-modules.txt"
  )
}
