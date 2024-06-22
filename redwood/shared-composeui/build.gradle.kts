plugins {
  kotlin("jvm")
  alias(libs.plugins.compose.compiler)
}

dependencies {
  api(projects.redwood.schema.widget)
  implementation(libs.redwood.widget.compose)
  implementation(libs.jetbrains.compose.material3)
  implementation(libs.jetbrains.compose.ui)
  implementation(libs.jetbrains.compose.ui.tooling.preview)
}
