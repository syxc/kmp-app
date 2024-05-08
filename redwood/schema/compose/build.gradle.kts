plugins {
  kotlin("multiplatform")
  id("app.cash.redwood.generator.compose")
}

val archivesBaseName = "schema-compose"

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
        api(libs.redwood.layout.compose)
        implementation(project(":redwood:schema:widget"))
      }
    }
  }
}

redwoodSchema {
  source.set(projects.redwood.schema)
  type.set("com.github.app.shared.redwood.Schema")
}

/* task 'testClasses' not found in project */
tasks.register("testClasses")
