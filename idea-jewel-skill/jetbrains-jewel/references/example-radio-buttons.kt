import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.RadioButtonRow

@Composable
fun RadioButtonsExample(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        RadioButtonRow(
            text = "Default",
            selected = selectedIndex == 0,
            onClick = { selectedIndex = 0 },
        )
        RadioButtonRow(
            text = "Error",
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            outline = Outline.Error,
        )
        RadioButtonRow(
            text = "Warning",
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 },
            outline = Outline.Warning,
        )
    }
}
