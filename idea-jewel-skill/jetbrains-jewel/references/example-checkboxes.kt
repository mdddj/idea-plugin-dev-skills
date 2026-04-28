import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.TriStateCheckboxRow

@Composable
fun CheckboxesExample(modifier: Modifier = Modifier) {
    var enabled by remember { mutableStateOf(true) }
    var triState by remember { mutableStateOf(ToggleableState.Off) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CheckboxRow(
            text = "Enable notifications",
            checked = enabled,
            onCheckedChange = { enabled = it },
        )

        TriStateCheckboxRow(
            "Project selection",
            triState,
            onClick = {
                triState = when (triState) {
                    ToggleableState.Off -> ToggleableState.Indeterminate
                    ToggleableState.Indeterminate -> ToggleableState.On
                    ToggleableState.On -> ToggleableState.Off
                }
            },
        )

        TriStateCheckboxRow(
            "Validation warning",
            triState,
            onClick = {},
            outline = Outline.Warning,
            enabled = false,
        )
    }
}
