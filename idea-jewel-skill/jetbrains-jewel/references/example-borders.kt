import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.Stroke
import org.jetbrains.jewel.foundation.modifier.border

@Composable
fun BordersExample(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(6.dp)

    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        androidx.compose.foundation.layout.Box(
            Modifier.size(48.dp).background(Color(0xFFE5E7EB), shape).border(
                Stroke.Alignment.Inside,
                1.dp,
                Color(0xFF6B7280),
                shape,
            )
        )

        androidx.compose.foundation.layout.Box(
            Modifier.size(48.dp).background(Color(0xFFDBEAFE), shape).border(
                Stroke.Alignment.Center,
                2.dp,
                Color(0xFF2563EB),
                shape,
            )
        )

        androidx.compose.foundation.layout.Box(
            Modifier.size(48.dp).background(Color(0xFFFEE2E2), shape).border(
                Stroke.Alignment.Outside,
                2.dp,
                Color(0xFFDC2626),
                shape,
            )
        )
    }
}
