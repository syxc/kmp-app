import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  kotlin("android")
  alias(libs.plugins.cashapp.redwood)
  id("com.jithub.build.logic")
}

android {
  namespace = "com.jithub.app.android"
  compileSdk = Versions.compileSdk
  defaultConfig {
    applicationId = "com.jithub.app.android"
    minSdk = Versions.minSdk
    targetSdk = Versions.targetSdk
    versionCode = Versions.versionCode
    versionName = Versions.versionName
  }

  buildFeatures {
    // The Redwood Gradle plugin cannot be applied to an Android project which enables Compose.
    // compose = true
    buildConfig = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = Versions.composeCompiler
  }

  packaging {
    resources {
      excludes += Resources.excludes
    }
  }

  val propertyFile = project.rootProject.file("keystore.properties")
  val currentSigning = if (propertyFile.exists() && propertyFile.canRead()) {
    val properties = Properties().apply {
      propertyFile.inputStream().use { load(it) }
    }
    if (file(properties["storeFile"] as String).canRead()) {
      signingConfigs.create("release") {
        storeFile = file(properties["storeFile"] as String)
        storePassword = properties["storePassword"] as String
        keyAlias = properties["keyAlias"] as String
        keyPassword = properties["keyPassword"] as String
      }
    } else {
      println("Oops,storeFile can't read!")
      signingConfigs.getByName("debug")
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

androidComponents {
  onVariants(selector().withBuildType("release")) {
    // Only exclude *.version files in release mode as debug mode requires
    // these files for layout inspector to work.
    it.packaging.resources.excludes.add("META-INF/*.version")
  }
}

dependencies {
  implementation(projects.shared)
  // implementation(projects.sharedCompose)
  implementation(libs.jetbrains.compose.ui)
  implementation(libs.jetbrains.compose.ui.tooling.preview)
  implementation(libs.jetbrains.compose.material3)
  implementation(libs.androidx.activity.compose)
  debugImplementation(libs.jetbrains.compose.ui.tooling)
}
