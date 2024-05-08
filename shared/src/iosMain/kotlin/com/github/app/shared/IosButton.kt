package com.github.app.shared

import app.cash.redwood.Modifier
import com.github.app.shared.redwood.widget.Button
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.UIKit.UIButton
import platform.UIKit.UIColor
import platform.UIKit.UIControlEventTouchUpInside
import platform.UIKit.UIControlStateNormal
import platform.UIKit.UIView
import platform.objc.sel_registerName

// NOTE: This class must be public for the click selector to work.
class IosButton : Button<UIView> {
  override val value = UIButton().apply {
    backgroundColor = UIColor.grayColor
  }

  override var modifier: Modifier = Modifier

  override fun text(text: String?) {
    value.setTitle(text, UIControlStateNormal)
  }

  override fun enabled(enabled: Boolean) {
    value.enabled = enabled
  }

  @OptIn(ExperimentalForeignApi::class)
  private val clickedPointer = sel_registerName("clicked")

  @OptIn(BetaInteropApi::class)
  @ObjCAction
  fun clicked() {
    onClick?.invoke()
  }

  private var onClick: (() -> Unit)? = null

  @OptIn(ExperimentalForeignApi::class)
  override fun onClick(onClick: (() -> Unit)?) {
    this.onClick = onClick
    if (onClick != null) {
      value.addTarget(this, clickedPointer, UIControlEventTouchUpInside)
    } else {
      value.removeTarget(this, clickedPointer, UIControlEventTouchUpInside)
    }
  }
}
