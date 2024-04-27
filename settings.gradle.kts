@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
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
  includeBuild("build-logic")
}

dependencyResolutionManagement {
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
  }
}

rootProject.name = "kmp-app"

include(":androidApp")
include(":composeApp")
include(":shared")

/* Encountering the “Unable to make progress running work” Error in Gradle? */
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
  "This project needs to be run with Java 17 or higher (found: ${JavaVersion.current()})."
}
