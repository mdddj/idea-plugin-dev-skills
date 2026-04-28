import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.painter.badge.DotBadgeShape
import org.jetbrains.jewel.ui.painter.hints.Badge
import org.jetbrains.jewel.ui.painter.hints.Size
import org.jetbrains.jewel.ui.painter.hints.Stroke

@Composable
fun IconsExample(modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(
            key = AllIconsKeys.Actions.AddFile,
            contentDescription = "Add",
            modifier = Modifier.size(16.dp),
        )

        Icon(
            key = AllIconsKeys.Nodes.ConfigFolder,
            contentDescription = "Folder with badge",
            modifier = Modifier.size(20.dp),
            hint = Badge(Color.Red, DotBadgeShape.Default),
        )

        Icon(
            key = AllIconsKeys.Nodes.ConfigFolder,
            contentDescription = "Folder stroked",
            modifier = Modifier.size(20.dp),
            hints = arrayOf(Stroke(Color.White), Size(18)),
        )
    }
}
