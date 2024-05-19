package com.jithub.app.shared.redwood.composeui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.cash.redwood.Modifier as RedwoodModifier
import com.jithub.app.shared.redwood.widget.Button

internal class ComposeUiButton : Button<@Composable () -> Unit> {
  private var text by mutableStateOf("")
  private var isEnabled by mutableStateOf(false)
  private var onClick by mutableStateOf({})

  override var modifier: RedwoodModifier = RedwoodModifier

  override val value = @Composable {
    Button(
      onClick = onClick,
      enabled = isEnabled,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text)
    }
  }

  override fun text(text: String?) {
    this.text = text ?: ""
  }

  override fun enabled(enabled: Boolean) {
    this.isEnabled = enabled
  }

  override fun onClick(onClick: (() -> Unit)?) {
    this.onClick = onClick ?: {}
  }
}
