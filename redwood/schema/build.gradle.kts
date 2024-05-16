plugins {
  kotlin("jvm")
  id("app.cash.redwood.schema")
}

dependencies {
  api(libs.redwood.layout.schema)
}

redwoodSchema {
  type.set("com.jithub.app.shared.redwood.Schema")
}
