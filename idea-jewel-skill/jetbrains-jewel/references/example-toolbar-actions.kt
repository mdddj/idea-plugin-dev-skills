import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.ActionButton
import org.jetbrains.jewel.ui.component.DropdownLink
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.SelectableIconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.component.separator
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ToolbarActionsExample(modifier: Modifier = Modifier) {
    val query = rememberTextFieldState("")
    var matchCase by remember { mutableStateOf(false) }
    var scope by remember { mutableStateOf("Current file") }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            state = query,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Filter items") },
            leadingIcon = {
                Icon(
                    key = AllIconsKeys.Actions.Find,
                    contentDescription = "Filter",
                )
            },
        )

        SelectableIconButton(
            selected = matchCase,
            onClick = { matchCase = !matchCase },
        ) {
            Icon(
                key = AllIconsKeys.Actions.MatchCase,
                contentDescription = "Match case",
            )
        }

        if (query.text.isNotEmpty()) {
            IconButton(onClick = { query.setTextAndPlaceCursorAtEnd("") }) {
                Icon(
                    key = AllIconsKeys.General.Close,
                    contentDescription = "Clear filter",
                )
            }
        }

        ActionButton(
            onClick = {},
            tooltip = { Text("Create a new toolbar action") },
        ) {
            Text("Generate")
        }

        DropdownLink(text = scope) {
            val scopes = listOf("Current file", "Module", "Project")
            scopes.forEachIndexed { index, item ->
                selectableItem(
                    selected = scope == item,
                    onClick = { scope = item },
                ) {
                    Text(item)
                }
                if (index != scopes.lastIndex) {
                    separator()
                }
            }
        }
    }
}
