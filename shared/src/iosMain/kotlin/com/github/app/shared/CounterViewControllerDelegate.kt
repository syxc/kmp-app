package com.github.app.shared

import app.cash.redwood.compose.DisplayLinkClock
import app.cash.redwood.compose.RedwoodComposition
import app.cash.redwood.layout.uiview.UIViewRedwoodLayoutWidgetFactory
import app.cash.redwood.widget.RedwoodUIView
import com.github.app.shared.core.StringList
import com.github.app.shared.redwood.widget.SchemaWidgetSystem
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus
import platform.UIKit.UIStackView

@Suppress("unused") // Called from Swift.
class CounterViewControllerDelegate(root: UIStackView) {
  private val scope = MainScope() + DisplayLinkClock

  init {
    initView(root)
  }

  private fun initView(root: UIStackView) {
    val composition = RedwoodComposition(
      scope = scope,
      view = RedwoodUIView(root),
      widgetSystem = SchemaWidgetSystem(
        Schema = IosWidgetFactory,
        RedwoodLayout = UIViewRedwoodLayoutWidgetFactory()
      )
    )

    composition.setContent {
      Counter(labels = StringList(listOfNotNull("Count:")))
    }

    /*val labelCount = MR.strings.label_count.desc().localized()
    composition.setContent {
      Counter(labels = StringList(listOfNotNull(labelCount)))
    }*/
  }

  fun dispose() {
    scope.cancel()
  }
}
