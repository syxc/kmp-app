import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  kotlin("android")
  id("com.github.build.logic")
}

android {
  namespace = "com.github.app.android"
  compileSdk = Versions.compileSdk
  defaultConfig {
    applicationId = "com.github.app.android"
    minSdk = Versions.minSdk
    targetSdk = Versions.targetSdk
    versionCode = Versions.versionCode
    versionName = Versions.versionName
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = Versions.composeCompiler
  }

  packaging {
    resources {
      excludes += Resources.excludes
    }
  }

  val currentSigning =
    if (rootProject.file("keystore.properties").exists()) {
      val properties =
        Properties().apply {
          rootProject.file("keystore.properties").inputStream().use { load(it) }
        }
      signingConfigs.create("release") {
        storeFile = file(properties["storeFile"] as String)
        storePassword = properties["storePassword"] as String
        keyAlias = properties["keyAlias"] as String
        keyPassword = properties["keyPassword"] as String
      }
    } else {
      signingConfigs.getByName("debug")
    }

  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
      isDebuggable = true
      isJniDebuggable = true
    }

    release {
      isDebuggable = false
      isJniDebuggable = false
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }

    all {
      signingConfig = currentSigning
    }
  }

  compileOptions {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
  }

  kotlinOptions {
    jvmTarget = Versions.java.toString()
  }
}

dependencies {
  implementation(projects.shared)
  implementation(projects.sharedCompose)
  implementation(libs.jetbrains.compose.ui)
  implementation(libs.jetbrains.compose.ui.tooling.preview)
  implementation(libs.jetbrains.compose.material3)
  implementation(libs.androidx.activity.compose)
  debugImplementation(libs.jetbrains.compose.ui.tooling)
}
