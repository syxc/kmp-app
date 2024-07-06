@file:Suppress("UnstableApiUsage")

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
  alias(libs.plugins.spotless) apply false
  id("com.jithub.gradle.build-logic")
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
  apply(plugin = "com.jithub.gradle.build-support")
}

subprojects {
  apply(from = rootProject.file("spotless/spotless.gradle"))
  afterEvaluate {
    tasks.withType(JavaCompile::class.java) {
      dependsOn(tasks.getByName("spotlessApply"))
    }
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
