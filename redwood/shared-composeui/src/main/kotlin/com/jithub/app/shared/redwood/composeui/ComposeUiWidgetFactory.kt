package com.jithub.app.shared.redwood.composeui

import androidx.compose.runtime.Composable
import com.jithub.app.shared.redwood.widget.Button
import com.jithub.app.shared.redwood.widget.SchemaWidgetFactory
import com.jithub.app.shared.redwood.widget.Text

object ComposeUiWidgetFactory : SchemaWidgetFactory<@Composable () -> Unit> {
  override fun Text(): Text<@Composable () -> Unit> = ComposeUiText()
  override fun Button(): Button<@Composable () -> Unit> = ComposeUiButton()
}
