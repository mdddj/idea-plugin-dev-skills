import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextArea

@Composable
fun TextAreaExample(modifier: Modifier = Modifier) {
    val state = rememberTextFieldState(
        "Jewel text area\n\nUse this for multi-line editable content."
    )

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextArea(
            state = state,
            modifier = Modifier.width(320.dp).height(180.dp),
        )

        TextArea(
            state = rememberTextFieldState(""),
            modifier = Modifier.width(320.dp).height(120.dp),
            outline = Outline.Error,
            placeholder = { Text("Validation error") },
        )
    }
}
