package com.jithub.app.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.cash.redwood.composeui.RedwoodContent
import app.cash.redwood.layout.composeui.ComposeUiRedwoodLayoutWidgetFactory
import com.jithub.app.shared.Counter
import com.jithub.app.shared.MR
import com.jithub.app.shared.core.StringList
import com.jithub.app.shared.redwood.composeui.ComposeUiWidgetFactory
import com.jithub.app.shared.redwood.widget.SchemaWidgetSystem

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val widgetSystem = SchemaWidgetSystem(
      Schema = ComposeUiWidgetFactory,
      RedwoodLayout = ComposeUiRedwoodLayoutWidgetFactory()
    )

    setContent {
      AppTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
          val labelCount = stringResource(MR.strings.label_count.resourceId)
          RedwoodContent(widgetSystem) {
            Counter(labels = StringList(listOfNotNull(labelCount)))
          }
        }
      }
    }
  }
}

@Composable
fun GreetingView(text: String, modifier: Modifier = Modifier) {
  Text(text = text)
}

@Preview
@Composable
private fun DefaultPreview() {
  AppTheme {
  }
}
