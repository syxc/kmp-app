@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

buildscript {
  repositories {
    mavenCentral()
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    gradlePluginPortal()
  }
  dependencies {
    classpath(libs.redwood.gradle.plugin)
    classpath(moko.resources.gradle.plugin)
  }
}

plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  // alias(libs.plugins.jetbrains.compose) apply false
  alias(libs.plugins.compose.compiler) apply false
  id("com.jithub.build.logic") apply false
}

allprojects {
  repositories {
    mavenCentral()
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    maven("https://jitpack.io")
  }
  configureCommonKotlin()
  configureCommonCompose()
}

gradle.taskGraph.whenReady {
  tasks.forEach {
    /* Encountering the “Unable to make progress running work” Error in Gradle? */
    // gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
    if (it.name.contains(":testClasses", ignoreCase = false)) {
      it.enabled = false
    }
  }
}

tasks.register<Delete>("clean") {
  delete(rootProject.layout.buildDirectory)
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
      jvmTarget = Versions.jvmTarget
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
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    kotlin.targets.withType(KotlinJvmTarget::class.java) {
      compilerOptions {
        freeCompilerArgs.set(
          freeCompilerArgs.getOrElse(emptyList()) + listOf(
            "-Xjdk-release=${Versions.javaVersion}"
          )
        )
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
