plugins {
  kotlin("jvm")
  id("app.cash.redwood.schema")
}

dependencies {
  api(libs.redwood.layout.schema)
}

redwoodSchema {
  type.set("com.ding1ding.app.shared.redwood.Schema")
}
