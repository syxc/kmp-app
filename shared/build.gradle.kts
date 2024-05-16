import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.cashapp.redwood)
  alias(libs.plugins.android.library)
  id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
  @OptIn(ExperimentalKotlinGradlePluginApi::class)
  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
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
    ios.deploymentTarget = "13.0"
    podfile = project.file("../iosApp/Podfile")
    framework {
      baseName = "shared"
      binaryOptions["bundleId"] = "com.jithub.app.shared"
      isStatic = true
      export(moko.resources)
      export("dev.icerock.moko:graphics:0.9.0")
    }
  }

  sourceSets {
    commonMain.dependencies {
      // Redwood
      implementation(libs.redwood.compose)
      implementation(libs.redwood.layout.compose)
      implementation(project(":redwood:schema:widget"))
      implementation(project(":redwood:schema:compose"))

      // api(moko.resources.compose)
      api(moko.resources)
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(project(":redwood:schema:testing"))
    }

    androidMain.dependencies {
      // Redwood
      api(project(":redwood:schema:widget"))
      api(project(":redwood:shared-composeui"))
      api(libs.redwood.composeui)
      api(libs.redwood.layout.composeui)
    }

    iosMain.dependencies {
      // redwood
      implementation(libs.redwood.layout.uiview)
    }
  }
}

multiplatformResources {
  resourcesPackage.set("com.jithub.app.shared")
}

android {
  namespace = "com.jithub.app.shared"
  compileSdk = Versions.compileSdk

  defaultConfig {
    minSdk = Versions.minSdk
  }

  buildFeatures {
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = Versions.java
    targetCompatibility = Versions.java
  }

  composeOptions {
    kotlinCompilerExtensionVersion = Versions.composeCompiler
  }
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
