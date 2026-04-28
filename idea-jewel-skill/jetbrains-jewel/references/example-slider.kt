import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.Slider

@Composable
fun SliderExample(modifier: Modifier = Modifier) {
    var freeValue by remember { mutableFloatStateOf(0.35f) }
    var steppedValue by remember { mutableFloatStateOf(30f) }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Slider(value = freeValue, onValueChange = { freeValue = it })
        Slider(
            value = steppedValue,
            onValueChange = { steppedValue = it },
            steps = 10,
            valueRange = 0f..100f,
        )
    }
}
