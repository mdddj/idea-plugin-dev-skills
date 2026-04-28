import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.bridge.theme.SwingBridgeTheme
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField

@OptIn(ExperimentalJewelApi::class)
@Composable
fun SwingBridgePanelExample(projectName: String, modifier: Modifier = Modifier) {
    val query = rememberTextFieldState("")
    var runCount by remember { mutableIntStateOf(0) }

    SwingBridgeTheme {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Current IDE theme: ${if (JewelTheme.isDark) "Dark" else "Light"}")
            Text("Project: $projectName")

            TextField(
                state = query,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search actions or files") },
            )

            DefaultButton(onClick = { runCount += 1 }) {
                Text("Run action ($runCount)")
            }

            OutlinedButton(
                onClick = { query.setTextAndPlaceCursorAtEnd(projectName) }
            ) {
                Text("Use project name")
            }
        }
    }
}
