package com.ding1ding.app.shared.redwood.composeui

import androidx.compose.runtime.Composable
import com.ding1ding.app.shared.redwood.widget.Button
import com.ding1ding.app.shared.redwood.widget.SchemaWidgetFactory
import com.ding1ding.app.shared.redwood.widget.Text

object ComposeUiWidgetFactory : SchemaWidgetFactory<@Composable () -> Unit> {
  override fun Text(): Text<@Composable () -> Unit> = ComposeUiText()
  override fun Button(): Button<@Composable () -> Unit> = ComposeUiButton()
}
