@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

if (gradle.gradleVersion >= "7.0" && gradle.gradleVersion < "8.0") {
  enableFeaturePreview("VERSION_CATALOGS")
}

dependencyResolutionManagement {
  versionCatalogs {
    create("libs") {
      from(files("../gradle/libs.versions.toml"))
    }
  }
}

rootProject.name = "build-logic"
include(":convention")
