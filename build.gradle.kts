import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
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
  id("com.github.build.logic") apply false
}

subprojects {
  tasks.withType<JavaCompile> {
    sourceCompatibility = Versions.java.toString()
    targetCompatibility = Versions.java.toString()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      jvmTarget = Versions.java.toString()
    }
  }
}

tasks.register<Delete>("clean") {
  delete(rootProject.layout.buildDirectory)
}
