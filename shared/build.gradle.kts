import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.android.library)
}

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser {
      commonWebpackConfig {
        devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
          static = (static ?: mutableListOf()).apply {
            // Serve sources to debug inside browser
            add(project.projectDir.path)
          }
        }
      }
    }
  }

  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = Versions.java.toString()
      }
    }
  }

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  cocoapods {
    summary = "Some description for the Shared Module"
    homepage = "Link to the Shared Module homepage"
    version = "1.0"
    ios.deploymentTarget = "14.0"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "SharedKit"
      isStatic = false // SwiftUI preview requires dynamic framework
      binaryOptions["bundleId"] = "com.github.app.shared"
      extraSpecAttributes["swift_version"] = "\"5.0\"" // <- SKIE Needs this!
    }
  }

  sourceSets {
    commonMain.dependencies {
      // put your multiplatform dependencies here
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
    }
  }
}

android {
  namespace = "com.github.app.shared"
  compileSdk = Versions.compileSdk
  compileOptions {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
  }
  defaultConfig {
    minSdk = Versions.minSdk
  }
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
