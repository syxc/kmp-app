import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

/**
 * Android application compose convention plugin
 *
 * If compose is required, then:
 * ```
 * plugins {
 *   id("com.android.application.compose")
 * }
 * ```
 *
 * If not required:
 * ```
 * plugins {
 *   id("com.android.application")
 * }
 * ```
 *
 * @constructor Create empty Android application compose convention plugin
 */
class AndroidApplicationComposeConventionPlugin : BasePlugin() {
  override fun apply(target: Project) {
    log("apply target: ${target.displayName}")
    with(target) {
      if (!plugins.hasPlugin("com.android.application")) {
        pluginManager.apply("com.android.application")
      }
      extensions.getByType<BaseAppModuleExtension>().apply {
        configureAndroidCompose(this)
      }
    }
  }
}
