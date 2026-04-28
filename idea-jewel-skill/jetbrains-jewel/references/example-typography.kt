import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun TypographyExample(modifier: Modifier = Modifier) {
    val typography = JewelTheme.typography

    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Heading 1", style = typography.h1TextStyle)
        Text("Heading 2", style = typography.h2TextStyle)
        Text("Regular body text", style = typography.regular)
        Text("Medium body text", style = typography.medium)
        Text("Small helper text", style = typography.small)
        Text("Editor text sample", style = typography.editorTextStyle)
    }
}
