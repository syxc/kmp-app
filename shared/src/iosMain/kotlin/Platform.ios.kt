import com.jithub.app.shared.MR
import dev.icerock.moko.resources.StringResource
import platform.UIKit.UIDevice

class IOSPlatform : Platform {
  override val name: String =
    UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
  override val mokoText: StringResource
    get() = MR.strings.hello_world
}

actual fun getPlatform(): Platform = IOSPlatform()
