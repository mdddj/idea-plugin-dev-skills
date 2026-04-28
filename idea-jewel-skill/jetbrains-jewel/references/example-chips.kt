import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.Chip
import org.jetbrains.jewel.ui.component.RadioButtonChip
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.ToggleableChip

@Composable
fun ChipsExample(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var toggled by remember { mutableStateOf(false) }
    var clickCount by remember { mutableIntStateOf(0) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RadioButtonChip(selected = selectedIndex == 0, onClick = { selectedIndex = 0 }, enabled = true) {
                Text("First")
            }
            RadioButtonChip(selected = selectedIndex == 1, onClick = { selectedIndex = 1 }, enabled = true) {
                Text("Second")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToggleableChip(checked = toggled, onClick = { toggled = it }, enabled = true) {
                Text("Toggleable")
            }
            Chip(enabled = true, onClick = { clickCount += 1 }) {
                Text("Clicks: $clickCount")
            }
        }
    }
}
