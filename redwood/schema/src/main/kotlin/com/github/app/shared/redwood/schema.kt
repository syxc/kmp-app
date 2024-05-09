@file:Suppress("ktlint:standard:filename")

package com.github.app.shared.redwood

import app.cash.redwood.layout.RedwoodLayout
import app.cash.redwood.schema.Default
import app.cash.redwood.schema.Property
import app.cash.redwood.schema.Schema
import app.cash.redwood.schema.Schema.Dependency
import app.cash.redwood.schema.Widget

@Schema(
  [
    Text::class,
    Button::class
  ],
  dependencies = [
    Dependency(1, RedwoodLayout::class)
  ]
)
interface Schema

@Widget(2)
data class Text(
  @Property(1) val text: String?
)

@Widget(3)
data class Button(
  @Property(1) val text: String?,
  @Property(2)
  @Default("true")
  val enabled: Boolean = true,
  @Property(3) val onClick: (() -> Unit)? = null
)
