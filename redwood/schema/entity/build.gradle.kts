plugins {
  kotlin("multiplatform")
}

kotlin {
  iosArm64()
  iosX64()
  iosSimulatorArm64()

//  js {
//    generateTypeScriptDefinitions()
//    useEsModules()
//    nodejs()
//    binaries.executable()
//  }

  jvm()

  sourceSets {
    val commonMain by getting {
      dependencies {
        // ignore
      }
    }
  }
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
