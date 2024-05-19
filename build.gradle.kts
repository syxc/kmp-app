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

  tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = Versions.java.toString()
    targetCompatibility = Versions.java.toString()
  }

  tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
      jvmTarget = Versions.java.toString()
    }
    /*compilerOptions {
      // Treat all Kotlin warnings as errors
      allWarningsAsErrors = true
      freeCompilerArgs.addAll(
        // Enable default methods in interfaces
        "-Xjvm-default=all",
        // Enable context receivers
        "-Xcontext-receivers",
        // Enable K2 compiler
        "-language-version=2.0",
        "-Xsuppress-version-warnings"
      )
    }*/
  }
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
