import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.DropdownLink
import org.jetbrains.jewel.ui.component.ExternalLink
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.separator

@Composable
fun LinksExample(modifier: Modifier = Modifier) {
    val docsUrl = "https://github.com/JetBrains/intellij-community/tree/master/platform/jewel/#readme"
    val themes = remember { listOf("Light", "Dark", "Darcula") }
    var currentTheme by remember { mutableStateOf(themes.first()) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Link(text = "Open details", onClick = {})
            ExternalLink(text = "Jewel README", uri = docsUrl)
            DropdownLink(text = "Theme: $currentTheme") {
                themes.forEachIndexed { index, item ->
                    selectableItem(
                        selected = currentTheme == item,
                        onClick = { currentTheme = item },
                    ) {
                        Text(item)
                    }
                    if (index != themes.lastIndex) {
                        separator()
                    }
                }
            }
        }
    }
}
