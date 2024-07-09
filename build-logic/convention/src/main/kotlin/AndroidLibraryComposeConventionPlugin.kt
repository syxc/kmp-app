import com.android.build.gradle.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Android library compose convention plugin
 *
 * If compose is required, then:
 * ```
 * plugins {
 *   id("com.android.library.compose")
 * }
 * ```
 *
 * If not required:
 * ```
 * plugins {
 *   id("com.android.library")
 * }
 * ```
 *
 * @constructor Create empty Android library compose convention plugin
 */
class AndroidLibraryComposeConventionPlugin : BasePlugin() {
  override fun apply(target: Project) {
    log("apply target: ${target.displayName}")
    with(target) {
      if (!plugins.hasPlugin("com.android.library")) {
        pluginManager.apply("com.android.library")
      }
      extensions.getByType<LibraryExtension>().apply {
        configureAndroidCompose(this)
      }
    }
  }
}
