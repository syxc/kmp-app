import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  kotlin("android")
  alias(libs.plugins.compose.compiler)
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
    // If you are using Redwood, you do not need to set this parameter.
    // compose = true
    buildConfig = true
  }

  packaging {
    resources {
      excludes += Resources.excludes
    }
  }

  val propertyFile by lazy { project.rootProject.file("keystore.properties") }
  val currentSigning = if (propertyFile.exists() && propertyFile.canRead()) {
    val properties = Properties().apply { propertyFile.inputStream().use { load(it) } }
    val currentStoreFile by lazy { file(properties.getProperty("storeFile")) }
    if (currentStoreFile.exists() && currentStoreFile.canRead()) {
      signingConfigs.create("release") {
        storeFile = currentStoreFile
        storePassword = properties.getProperty("storePassword")
        keyAlias = properties.getProperty("keyAlias")
        keyPassword = properties.getProperty("keyPassword")
      }
    } else {
      println("Oops, storeFile can't be read!")
      signingConfigs.getByName("debug")
    }
  } else {
    println("Oops, keystore.properties can't be read!")
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
    sourceCompatibility = Versions.javaVersion
    targetCompatibility = Versions.javaVersion
  }

  kotlinOptions {
    jvmTarget = Versions.javaVersion.toString()
  }
}

composeCompiler {
  enableStrongSkippingMode = true
  reportsDestination = layout.buildDirectory.dir("compose_compiler")
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
