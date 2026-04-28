import androidx.compose.foundation.gestures.ScrollableState
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
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.intui.markdown.bridge.ProvideMarkdownStyling
import org.jetbrains.jewel.markdown.LazyMarkdown
import org.jetbrains.jewel.markdown.MarkdownBlock
import org.jetbrains.jewel.markdown.MarkdownMode
import org.jetbrains.jewel.markdown.processing.MarkdownProcessor
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding
import javax.swing.JComponent

@OptIn(ExperimentalJewelApi::class)
fun createBridgeMarkdownPreviewPanel(project: Project, rawMarkdown: String): JComponent = JewelComposePanel {
    BridgeMarkdownPreviewExample(
        project = project,
        rawMarkdown = rawMarkdown,
    )
}

@OptIn(ExperimentalJewelApi::class)
@Composable
fun BridgeMarkdownPreviewExample(
    project: Project,
    rawMarkdown: String,
    modifier: Modifier = Modifier,
) {
    val markdownMode = remember { MarkdownMode.EditorPreview(null) }
    val processor = remember {
        MarkdownProcessor(markdownMode = markdownMode)
    }
    var blocks by remember { mutableStateOf<List<MarkdownBlock>>(emptyList()) }

    LaunchedEffect(rawMarkdown) {
        blocks = withContext(Dispatchers.Default) {
            processor.processMarkdownDocument(rawMarkdown)
        }
    }

    ProvideMarkdownStyling(
        project = project,
        markdownMode = markdownMode,
        markdownProcessor = processor,
    ) {
        val lazyListState = rememberLazyListState()

        VerticallyScrollableContainer(
            lazyListState as ScrollableState,
            modifier = modifier.fillMaxSize(),
        ) {
            LazyMarkdown(
                markdownBlocks = blocks,
                state = lazyListState,
                selectable = true,
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 12.dp,
                    end = 12.dp + scrollbarContentSafePadding(),
                    bottom = 12.dp,
                ),
            )
        }
    }
}
