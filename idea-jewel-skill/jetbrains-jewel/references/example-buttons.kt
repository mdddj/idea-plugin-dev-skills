import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.ActionButton
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.SelectableIconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ButtonsExample(modifier: Modifier = Modifier) {
    var selected by remember { mutableStateOf(false) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(onClick = {}) { Text("Outlined") }
            DefaultButton(onClick = {}) { Text("Default") }

            IconButton(onClick = {}) {
                Icon(
                    key = AllIconsKeys.Actions.AddFile,
                    contentDescription = "Add",
                )
            }

            SelectableIconButton(
                selected = selected,
                onClick = { selected = !selected },
            ) {
                Icon(
                    key = AllIconsKeys.Actions.MatchCase,
                    contentDescription = "Toggle selection",
                )
            }
        }

        ActionButton(
            onClick = {},
            tooltip = { Text("Run a secondary action") },
        ) {
            Text("Hover me")
        }
    }
}
