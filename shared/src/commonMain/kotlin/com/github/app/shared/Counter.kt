package com.github.app.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.redwood.Modifier
import app.cash.redwood.layout.api.Constraint
import app.cash.redwood.layout.api.CrossAxisAlignment
import app.cash.redwood.layout.api.MainAxisAlignment
import app.cash.redwood.layout.compose.Column
import com.ding1ding.app.shared.redwood.compose.Button
import com.ding1ding.app.shared.redwood.compose.Text
import com.github.app.shared.core.StringList

@Composable
fun Counter(
  modifier: Modifier = Modifier,
  value: Int = 0,
  labels: StringList? = StringList(listOf())
) {
  var count by rememberSaveable { mutableIntStateOf(value) }
  Column(
    width = Constraint.Fill,
    height = Constraint.Fill,
    horizontalAlignment = CrossAxisAlignment.Center,
    verticalAlignment = MainAxisAlignment.Center,
    modifier = modifier
  ) {
    Button("-1", onClick = { count-- })
    Text(text = "${labels?.items?.get(0)} $count")
    Button("+1", onClick = { count++ })
  }
}
