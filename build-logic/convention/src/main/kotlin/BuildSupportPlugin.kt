import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class BuildSupportPlugin : BasePlugin() {

  override fun apply(target: Project) {
    log("apply target: ${target.displayName}")

    target.group = "com.jithub.build-support"
    target.version = "0.1"

    target.configureCommonKotlin()
    target.configureCommonCompose()
  }

  // https://github.com/cashapp/redwood/blob/trunk/build-support/src/main/kotlin/app/cash/redwood/buildsupport/RedwoodBuildPlugin.kt
  private fun Project.configureCommonKotlin() {
    tasks.withType(KotlinCompile::class.java).configureEach {
      compilerOptions {
        // Treat all Kotlin warnings as errors (disabled by default)
        allWarningsAsErrors.set(properties["warningsAsErrors"] as? Boolean ?: false)

        freeCompilerArgs.set(
          freeCompilerArgs.getOrElse(emptyList()) + listOf(
            // https://kotlinlang.org/docs/whatsnew13.html#progressive-mode
            "-progressive",
            // https://kotlinlang.org/docs/multiplatform-expect-actual.html#expected-and-actual-classes
            "-Xexpect-actual-classes"
          )
        )
      }
    }

    tasks.withType(KotlinJvmCompile::class.java).configureEach {
      compilerOptions {
        freeCompilerArgs.set(
          freeCompilerArgs.getOrElse(emptyList()) + listOf(
            "-Xjvm-default=all"
          )
        )
        jvmTarget.set(Versions.jvmTarget)
      }
    }

    // Kotlin requires the Java compatibility matches.
    tasks.withType(JavaCompile::class.java).configureEach {
      sourceCompatibility = Versions.javaVersion.toString()
      targetCompatibility = Versions.javaVersion.toString()
    }

    plugins.withId("org.jetbrains.kotlin.multiplatform") {
      val kotlin = extensions.getByName("kotlin") as KotlinMultiplatformExtension

      // We set the JVM target (the bytecode version) above for all Kotlin-based Java bytecode
      // compilations, but we also need to set the JDK API version for the Kotlin JVM targets to
      // prevent linking against newer JDK APIs (the Android targets link against the android.jar).
      kotlin.targets.withType(KotlinJvmTarget::class.java) {
        compilations.configureEach {
          compileTaskProvider.configure {
            compilerOptions {
              freeCompilerArgs.set(
                freeCompilerArgs.getOrElse(emptyList()) + listOf(
                  "-Xjdk-release=${Versions.javaVersion}"
                )
              )
            }
          }
        }
      }

      kotlin.targets.withType(KotlinNativeTarget::class.java) {
        binaries.withType(Framework::class.java) {
          linkerOpts += "-lsqlite3"
        }
      }
    }
  }

  /**
   * Force Android Compose UI and JetPack Compose UI usage to Compose compiler versions which are compatible
   * with the project's Kotlin version.
   */
  private fun Project.configureCommonCompose() {
    tasks.withType<KotlinJsCompile>().configureEach {
      compilerOptions {
        freeCompilerArgs.set(
          freeCompilerArgs.getOrElse(emptyList()) + listOf(
            // https://github.com/JetBrains/compose-multiplatform/issues/3421
            "-Xpartial-linkage=disable",
            // https://github.com/JetBrains/compose-multiplatform/issues/3418
            "-Xklib-enable-signature-clash-checks=false",
            // Translate capturing lambdas into anonymous JS functions rather than hoisting parameters
            // and creating a named sibling function. Only affects targets which produce actual JS.
            "-Xir-generate-inline-anonymous-functions"
          )
        )
      }
    }
  }
}
