@file:Suppress("UnstableApiUsage")

plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  google {
    content {
      includeGroupByRegex(".*google.*")
      includeGroupByRegex(".*android.*")
    }
  }
}

group = "com.github.app.buildlogic"

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
      id = "com.github.build.logic"
      implementationClass = "BuildLogic"
    }
  }
}
