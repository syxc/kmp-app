import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.dsl.Lint
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.TestExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

class BuildSupportPlugin : BasePlugin() {

  override fun apply(project: Project) {
    log("apply target: ${project.displayName}")

    project.group = "com.jithub.app.build-support"
    project.version = Versions.versionName

    project.configureCommonAndroid()
    project.configureCommonKotlin()
    project.configureCommonCompose()
  }

  private fun Project.configureCommonAndroid() {
    plugins.withId("com.android.base") {
      val android = extensions.getByName("android") as BaseExtension
      android.apply {
        compileSdkVersion(Versions.compileSdk)
        defaultConfig {
          minSdk = Versions.minSdk
          targetSdk = Versions.targetSdk
        }

        testOptions.animationsDisabled = true

        sourceSets.configureEach {
          java.srcDirs("src/$name/kotlin")
        }

        packagingOptions.apply {
          resources {
            excludes += Resources.excludes
          }
        }

        compileOptions {
          sourceCompatibility = Versions.javaVersion
          targetCompatibility = Versions.javaVersion
        }

        tasks.withType(KotlinJvmCompile::class.java).configureEach {
          compilerOptions {
            // Treat all Kotlin warnings as errors (disabled by default)
            allWarningsAsErrors.set(properties["warningsAsErrors"] as? Boolean ?: false)

            freeCompilerArgs.set(
              freeCompilerArgs.getOrElse(emptyList()) + listOf(
                "-Xcontext-receivers",
                "-opt-in=kotlin.RequiresOptIn",
                // Enable experimental coroutines APIs, including Flow
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                // Enable experimental compose APIs
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.lifecycle.compose.ExperimentalLifecycleComposeApi",
                "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi"
              )
            )

            jvmTarget.set(Versions.jvmTarget)
          }
        }

        with(project) {
          when (android) {
            is BaseAppModuleExtension -> configure<BaseAppModuleExtension> {
              lint(lintConfigure())
            }

            is LibraryExtension -> configure<LibraryExtension> {
              lint(lintConfigure())
            }

            is TestExtension -> configure<TestExtension> {
              lint(lintConfigure())
            }

            else -> {
              pluginManager.apply("com.android.lint")
              configure<Lint>(lintConfigure())
            }
          }
        }
      }
    }

    plugins.withId("com.android.application") {
      val android = extensions.getByName("android") as BaseAppModuleExtension
      android.apply {
        dependenciesInfo {
          // Disables dependency metadata when building APKs.
          includeInApk = false
          // Disables dependency metadata when building Android App Bundles.
          includeInBundle = false
        }
      }

      val androidComponents = extensions.getByType(AndroidComponentsExtension::class.java)
      with(androidComponents) {
        onVariants(selector().withBuildType("release")) {
          // Only exclude *.version files in release mode as debug mode requires
          // these files for layout inspector to work.
          it.packaging.resources.excludes.add("META-INF/*.version")
        }
      }
    }
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

  private fun lintConfigure(): Lint.() -> Unit = {
    abortOnError = true
    warningsAsErrors = false
    ignoreTestSources = true
    checkDependencies = true
    checkReleaseBuilds = false // Full lint runs as part of 'build' task.
    htmlReport = true
  }
}
