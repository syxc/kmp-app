@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    classpath(moko.resources.gradle.plugin)
  }
}

plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  // alias(libs.plugins.jetbrains.compose) apply false
  alias(libs.plugins.cashapp.redwood) apply false
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

private fun Project.configureCommonKotlin() {
  // Kotlin requires the Java compatibility matches.
  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = Versions.javaVersion.toString()
    targetCompatibility = Versions.javaVersion.toString()
  }

  tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
      // Treat all Kotlin warnings as errors (disabled by default)
      allWarningsAsErrors.set(properties["warningsAsErrors"] as? Boolean ?: false)

      freeCompilerArgs.set(
        freeCompilerArgs.getOrElse(emptyList()) + listOf(
          // Enable default methods in interfaces
          "-Xjvm-default=all",
          // Enable context receivers
          "-Xcontext-receivers",
          // Enable K2 compiler
          "-language-version=2.0",
          "-Xsuppress-version-warnings",
          // https://kotlinlang.org/docs/whatsnew13.html#progressive-mode
          "-progressive",
          // https://kotlinlang.org/docs/multiplatform-expect-actual.html#expected-and-actual-classes
          "-Xexpect-actual-classes"
        )
      )

      jvmTarget.set(Versions.jvmTarget)
    }
  }
}

/**
 * Force Android Compose UI and JetPack Compose UI usage to Compose compiler versions which are compatible
 * with the project's Kotlin version.
 */
private fun Project.configureCommonCompose() {
  tasks.withType<KotlinJsCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
      // https://github.com/JetBrains/compose-multiplatform/issues/3421
      "-Xpartial-linkage=disable",
      // https://github.com/JetBrains/compose-multiplatform/issues/3418
      "-Xklib-enable-signature-clash-checks=false",
      // Translate capturing lambdas into anonymous JS functions rather than hoisting parameters
      // and creating a named sibling function. Only affects targets which produce actual JS.
      "-Xir-generate-inline-anonymous-functions"
    )
  }
}
