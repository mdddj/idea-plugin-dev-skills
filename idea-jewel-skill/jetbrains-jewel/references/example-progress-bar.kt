import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.CircularProgressIndicator
import org.jetbrains.jewel.ui.component.HorizontalProgressBar
import org.jetbrains.jewel.ui.component.IndeterminateHorizontalProgressBar
import org.jetbrains.jewel.ui.component.Text

@Composable
fun ProgressBarExample(modifier: Modifier = Modifier) {
    var progress by remember { mutableFloatStateOf(0.45f) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            HorizontalProgressBar(
                progress = progress,
                modifier = Modifier.width(240.dp),
            )
            Text("${(progress * 100).toInt()} %")
        }

        IndeterminateHorizontalProgressBar(modifier = Modifier.width(240.dp))

        CircularProgressIndicator()
    }
}
