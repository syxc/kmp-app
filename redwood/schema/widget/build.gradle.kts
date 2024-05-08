plugins {
  kotlin("multiplatform")
  id("app.cash.redwood.generator.widget")
}

val archivesBaseName = "schema-widget"

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
    commonMain {
      dependencies {
        api(libs.redwood.layout.widget)
      }
    }
  }
}

redwoodSchema {
  source.set(projects.redwood.schema)
  type.set("com.ding1ding.app.shared.redwood.Schema")
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
