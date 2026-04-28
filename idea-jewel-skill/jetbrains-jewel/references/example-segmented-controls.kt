import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.SegmentedControl
import org.jetbrains.jewel.ui.component.SegmentedControlButtonData
import org.jetbrains.jewel.ui.component.Text

@Composable
fun SegmentedControlsExample(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val buttonIds = listOf(0, 1, 2)
    val buttons =
        buttonIds.map { index ->
            SegmentedControlButtonData(
                selected = index == selectedIndex,
                content = { _ -> Text("Mode ${index + 1}") },
                onSelect = { selectedIndex = index },
            )
        }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SegmentedControl(buttons = buttons, enabled = true)
        SegmentedControl(buttons = buttons, enabled = false)
    }
}
