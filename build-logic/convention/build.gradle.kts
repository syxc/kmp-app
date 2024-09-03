@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  gradlePluginPortal()
  google()
}

group = "com.jithub.app.build-logic"

// If we use jvmToolchain, we need to install JDK 11
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "11"

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
  compileOnly(libs.gradlePlugin.android) {
    exclude(group = "org.ow2.asm")
  }
  compileOnly(libs.gradlePlugin.kotlin) {
    exclude(group = "org.ow2.asm")
  }
  compileOnly(libs.bundles.asm)
  implementation(libs.gradlePlugin.spotless)
  implementation(libs.gradlePlugin.compose.compiler)
  implementation(files(libs::class.java.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
  plugins {
    val prefix = "com.jithub.gradle"
    register("buildLogic") {
      id = "$prefix.build-logic"
      implementationClass = "BuildLogic"
    }
    register("buildSupport") {
      id = "$prefix.build-support"
      implementationClass = "BuildSupportPlugin"
    }

    register("androidApplicationCompose") {
      id = "com.android.application.compose"
      implementationClass = "AndroidApplicationComposeConventionPlugin"
    }
    register("androidLibraryCompose") {
      id = "com.android.library.compose"
      implementationClass = "AndroidLibraryComposeConventionPlugin"
    }
  }
}
