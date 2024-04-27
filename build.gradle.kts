plugins {
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.kotlin.cocoapods) apply false
  alias(libs.plugins.jetbrains.compose) apply false
  id("com.github.build.logic") apply false
}
