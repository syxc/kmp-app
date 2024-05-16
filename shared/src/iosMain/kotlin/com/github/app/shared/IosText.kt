package com.jithub.app.shared

import app.cash.redwood.Modifier
import com.jithub.app.shared.redwood.widget.Text
import platform.UIKit.NSTextAlignmentCenter
import platform.UIKit.UILabel
import platform.UIKit.UIView

class IosText : Text<UIView> {
  override val value = UILabel().apply {
    // textColor = UIColor.blackColor // TODO why is this needed?
    textAlignment = NSTextAlignmentCenter
  }

  override var modifier: Modifier = Modifier

  override fun text(text: String?) {
    value.text = text

    // This very simple integration wraps the size of whatever text is entered. Calling
    // this function will update the bounds and trigger relayout in the parent.
    value.sizeToFit()
  }
}
