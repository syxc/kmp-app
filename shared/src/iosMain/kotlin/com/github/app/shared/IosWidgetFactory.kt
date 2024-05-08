package com.github.app.shared

import com.github.app.shared.redwood.widget.Button
import com.github.app.shared.redwood.widget.SchemaWidgetFactory
import com.github.app.shared.redwood.widget.Text
import platform.UIKit.UIView

object IosWidgetFactory : SchemaWidgetFactory<UIView> {
  override fun Text(): Text<UIView> = IosText()
  override fun Button(): Button<UIView> = IosButton()
}
