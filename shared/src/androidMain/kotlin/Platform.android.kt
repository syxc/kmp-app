import android.os.Build
import com.github.app.shared.MR
import dev.icerock.moko.resources.StringResource

class AndroidPlatform : Platform {
  override val name: String = "Android ${Build.VERSION.SDK_INT}"
  override val mokoText: StringResource
    get() = MR.strings.hello_world
}

actual fun getPlatform(): Platform = AndroidPlatform()
