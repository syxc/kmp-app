import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.kotlin.cocoapods)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.android.library)
  id("dev.icerock.mobile.multiplatform-resources")
}

kotlin {
  // https://youtrack.jetbrains.com/issue/KT-61573
  targets.configureEach {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions {
          freeCompilerArgs.add("-Xexpect-actual-classes")
        }
      }
    }
  }

  androidTarget {
    @Suppress("OPT_IN_USAGE")
    unitTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
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
      baseName = "SharedKit"
      binaryOptions["bundleId"] = "com.jithub.app.shared"
      isStatic = true
      export(moko.resources)
      export(moko.graphics)
    }
    extraSpecAttributes["swift_version"] = "[\'5.0\']" // SKIE Needs this!
  }

  sourceSets {
    all {
      languageSettings.apply {
        optIn("kotlin.RequiresOptIn")
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        optIn("kotlin.time.ExperimentalTime")
      }
    }

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
      // Redwood
      implementation(libs.redwood.layout.uiview)
    }
  }
}

multiplatformResources {
  resourcesPackage.set("com.jithub.app.shared")
}

android {
  namespace = "com.jithub.app.shared"

  defaultConfig {
  }

  buildFeatures {
    buildConfig = true
  }
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
