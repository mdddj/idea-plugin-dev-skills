import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.HorizontalSplitLayout
import org.jetbrains.jewel.ui.component.SplitLayoutState
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticalSplitLayout

@Composable
fun SplitLayoutExample(modifier: Modifier = Modifier) {
    val horizontalState = remember { SplitLayoutState(0.4f) }
    val verticalState = remember { SplitLayoutState(0.5f) }

    HorizontalSplitLayout(
        state = horizontalState,
        modifier = modifier.fillMaxSize(),
        firstPaneMinWidth = 160.dp,
        secondPaneMinWidth = 160.dp,
        first = {
            Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Left pane")
            }
        },
        second = {
            VerticalSplitLayout(
                state = verticalState,
                modifier = Modifier.fillMaxSize(),
                firstPaneMinWidth = 120.dp,
                secondPaneMinWidth = 120.dp,
                first = {
                    Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("Top right pane")
                    }
                },
                second = {
                    Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                        Text("Bottom right pane")
                    }
                },
            )
        },
    )
}
