@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
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
  implementation(libs.bundles.asm)
}

gradlePlugin {
  plugins {
    register("build-logic") {
      id = "com.jithub.build.logic"
      implementationClass = "BuildLogic"
    }
  }
}
