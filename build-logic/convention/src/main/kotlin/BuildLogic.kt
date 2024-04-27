import org.gradle.api.Project

class BuildLogic : BasePlugin() {
  override fun apply(project: Project) {
    log("======== start apply ========")
    log("apply target: ${project.displayName}")
    log("java sourceCompatibility: ${Versions.java}")
    log("======== end apply ========")
  }
}
