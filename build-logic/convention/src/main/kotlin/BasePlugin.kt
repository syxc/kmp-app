import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class BasePlugin : Plugin<Project> {
  internal fun log(msg: String) {
    println("[${this.javaClass.simpleName}]: $msg")
  }
}
