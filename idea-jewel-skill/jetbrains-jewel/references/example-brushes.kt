import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.Stroke
import org.jetbrains.jewel.foundation.modifier.border
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Slider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.graphics.cssLinearGradient

@Composable
fun BrushesExample(modifier: Modifier = Modifier) {
    var angleDegrees by remember { mutableFloatStateOf(25f) }
    var scaleX by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    val brush = remember(angleDegrees, scaleX, offsetX) {
        Brush.cssLinearGradient(
            angleDegrees = angleDegrees.toDouble(),
            colors = listOf(
                Color(0xff2563eb),
                Color(0xff06b6d4),
                Color(0xff22c55e),
            ),
            stops = listOf(0f, 0.45f, 1f),
            scaleX = scaleX,
            scaleY = 1f,
            offset = Offset(offsetX, 0f),
        )
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Angle: ${angleDegrees.toInt()} deg")
        Slider(
            value = angleDegrees,
            onValueChange = { angleDegrees = it },
            valueRange = 0f..180f,
        )

        Text("Scale X: $scaleX")
        Slider(
            value = scaleX,
            onValueChange = { scaleX = it },
            valueRange = 0.5f..3f,
        )

        Text("Offset X: $offsetX")
        Slider(
            value = offsetX,
            onValueChange = { offsetX = it },
            valueRange = -1f..1f,
        )

        Box(
            modifier = Modifier.fillMaxWidth()
                .height(112.dp)
                .border(
                    alignment = Stroke.Alignment.Inside,
                    width = 1.dp,
                    color = JewelTheme.globalColors.borders.normal,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(1.dp)
                .background(brush, RoundedCornerShape(9.dp))
        )
    }
}
