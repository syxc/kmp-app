@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
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
}

dependencyResolutionManagement {
  repositories {
    mavenCentral()
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
  }
  versionCatalogs {
    create("moko") {
      from(files("gradle/moko.versions.toml"))
    }
  }
}

rootProject.name = "kmp-app"

include(":androidApp")
// include(":shared-compose")
include(":shared")
// redwood
include(":redwood:schema")
include(":redwood:schema:entity")
include(":redwood:schema:compose")
include(":redwood:schema:widget")
include(":redwood:schema:testing")
include(":redwood:shared-composeui")

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
  "This project needs to be run with Java 17 or higher (found: ${JavaVersion.current()})."
}
