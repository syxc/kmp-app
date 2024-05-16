import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.library)
  alias(libs.plugins.jetbrains.compose)
}

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    moduleName = "composeApp"
    browser {
      commonWebpackConfig {
        outputFileName = "composeApp.js"
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
          static = (static ?: mutableListOf()).apply {
            // Serve sources to debug inside browser
            add(project.projectDir.path)
          }
        }
      }
    }
    binaries.executable()
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = Versions.java.toString()
      }
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(projects.shared)
    }

    androidMain.dependencies {
      implementation(libs.jetbrains.compose.ui.tooling.preview)
      implementation(libs.androidx.activity.compose)
    }
  }
}

android {
  namespace = "com.jithub.app.share"
  compileSdk = Versions.compileSdk

  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  sourceSets["main"].res.srcDirs("src/androidMain/res")
  sourceSets["main"].resources.srcDirs("src/commonMain/resources")

  defaultConfig {
    // applicationId = "com.jithub.app.share"
    minSdk = Versions.minSdk
  }

  packaging {
    resources {
      excludes += Resources.excludes
    }
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }

  compileOptions {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
  }

  dependencies {
    debugImplementation(libs.jetbrains.compose.ui.tooling)
  }
}

compose.experimental {
  web.application {}
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
