@file:Suppress("UnstableApiUsage")

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
}
