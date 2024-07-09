import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.Lint
import org.gradle.api.Project
import org.gradle.api.plugins.PluginContainer
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.lintConfigure(): Lint.() -> Unit = {
  abortOnError = true
  warningsAsErrors = false
  ignoreTestSources = true
  checkDependencies = true
  checkReleaseBuilds = false // Full lint runs as part of 'build' task.
  htmlReport = true
  // baseline = rootProject.file("lint-config/lint-baseline.xml")
  // lintConfig = rootProject.file("lint-config/lint-default.xml")
}

internal fun PluginContainer.hasKotlinComposePlugin() = hasPlugin("org.jetbrains.kotlin.plugin.compose")

/**
 * Configure Compose-specific options
 *
 * @param commonExtension
 * @param hasRedwood Is Redwood used?
 */
fun Project.configureAndroidCompose(
  commonExtension: CommonExtension<*, *, *, *, *>,
  hasRedwood: Boolean = true
) {
  if (!plugins.hasKotlinComposePlugin()) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
  }

  commonExtension.apply {
    if (!hasRedwood) {
      // If you are using Redwood, you do not need to set this parameter.
      buildFeatures.compose = true
    }
  }

  extensions.configure<ComposeCompilerGradlePluginExtension> {
    enableStrongSkippingMode.set(true)
    reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
  }
}
