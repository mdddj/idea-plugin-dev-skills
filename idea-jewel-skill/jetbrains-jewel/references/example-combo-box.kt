import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.EditableListComboBox
import org.jetbrains.jewel.ui.component.ListComboBox
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ComboBoxExample(modifier: Modifier = Modifier) {
    val items = remember { listOf("Kotlin", "Java", "Python", "Rust") }
    var selectedIndex by remember { mutableIntStateOf(0) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Language")

        ListComboBox(
            items = items,
            selectedIndex = selectedIndex,
            onSelectedItemChange = { selectedIndex = it },
            modifier = Modifier.widthIn(max = 240.dp).fillMaxWidth(),
            itemKeys = { _, item -> item },
        )

        EditableListComboBox(
            items = items,
            selectedIndex = selectedIndex,
            onSelectedItemChange = { selectedIndex = it },
            modifier = Modifier.widthIn(max = 240.dp).fillMaxWidth(),
        )
    }
}
