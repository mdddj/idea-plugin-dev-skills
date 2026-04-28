import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.jewel.foundation.code.highlighting.NoOpCodeHighlighter
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.markdown.standalone.ProvideMarkdownStyling
import org.jetbrains.jewel.intui.markdown.standalone.dark
import org.jetbrains.jewel.intui.markdown.standalone.light
import org.jetbrains.jewel.markdown.LazyMarkdown
import org.jetbrains.jewel.markdown.MarkdownBlock
import org.jetbrains.jewel.markdown.extensions.autolink.AutolinkProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.alerts.GitHubAlertProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.strikethrough.GitHubStrikethroughProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.tables.GitHubTableProcessorExtension
import org.jetbrains.jewel.markdown.processing.MarkdownProcessor
import org.jetbrains.jewel.markdown.rendering.MarkdownBlockRenderer
import org.jetbrains.jewel.markdown.rendering.MarkdownStyling

@Composable
fun MarkdownPreviewExample(rawMarkdown: String, modifier: Modifier = Modifier) {
    val isDark = JewelTheme.isDark
    val styling = remember(isDark) {
        if (isDark) MarkdownStyling.dark() else MarkdownStyling.light()
    }
    val processor = remember {
        MarkdownProcessor(
            listOf(
                AutolinkProcessorExtension,
                GitHubAlertProcessorExtension,
                GitHubStrikethroughProcessorExtension(),
                GitHubTableProcessorExtension,
            )
        )
    }

    var blocks by remember { mutableStateOf<List<MarkdownBlock>>(emptyList()) }

    LaunchedEffect(rawMarkdown) {
        blocks = withContext(Dispatchers.Default) {
            processor.processMarkdownDocument(rawMarkdown)
        }
    }

    val blockRenderer = remember(styling, isDark) {
        if (isDark) {
            MarkdownBlockRenderer.dark(styling = styling)
        } else {
            MarkdownBlockRenderer.light(styling = styling)
        }
    }

    ProvideMarkdownStyling(styling, blockRenderer, NoOpCodeHighlighter) {
        LazyMarkdown(
            blocks = blocks,
            state = rememberLazyListState(),
            modifier = modifier.fillMaxSize().background(if (isDark) Color(0xff0d1117) else Color.White),
            contentPadding = PaddingValues(12.dp),
            selectable = true,
        )
    }
}
