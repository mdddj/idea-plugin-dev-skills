import androidx.compose.foundation.TooltipPlacement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip

@Composable
fun TooltipExample() {
    Tooltip(
        tooltip = { Text("This action is safe to run") },
        tooltipPlacement = TooltipPlacement.ComponentRect(Alignment.BottomCenter, Alignment.TopCenter),
    ) {
        DefaultButton(onClick = {}) {
            Text("Hover me")
        }
    }
}
