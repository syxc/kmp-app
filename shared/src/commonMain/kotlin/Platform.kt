import dev.icerock.moko.resources.StringResource

interface Platform {
  val name: String
  val mokoText: StringResource
}

expect fun getPlatform(): Platform
