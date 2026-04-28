import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.TextArea

@Composable
fun MarkdownSplitEditorExample() {
    val editorState = rememberTextFieldState(
        "# Jewel Markdown\n\n- Edit on the left\n- Preview on the right\n"
    )

    Row(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        MarkdownEditorPane(
            state = editorState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
        )

        MarkdownPreviewExample(
            rawMarkdown = editorState.text.toString(),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MarkdownEditorPane(state: TextFieldState, modifier: Modifier = Modifier) {
    TextArea(
        state = state,
        modifier = modifier,
        textStyle = JewelTheme.editorTextStyle,
        undecorated = false,
    )
}
