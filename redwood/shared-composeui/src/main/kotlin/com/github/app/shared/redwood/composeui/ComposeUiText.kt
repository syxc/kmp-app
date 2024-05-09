package com.github.app.shared.redwood.composeui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.cash.redwood.Modifier
import com.github.app.shared.redwood.widget.Text

internal class ComposeUiText : Text<@Composable () -> Unit> {
  private var text by mutableStateOf("")

  override var modifier: Modifier = Modifier

  override val value = @Composable {
    Text(
      text = text
    )
  }

  override fun text(text: String?) {
    this.text = text ?: ""
  }
}
