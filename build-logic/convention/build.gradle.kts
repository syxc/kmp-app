@file:Suppress("UnstableApiUsage")

plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  google()
}

group = "com.jithub.app.build-logic"

kotlin {
  jvmToolchain(17)
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
