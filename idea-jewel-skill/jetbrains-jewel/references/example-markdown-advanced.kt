import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.code.highlighting.NoOpCodeHighlighter
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.markdown.standalone.ProvideMarkdownStyling
import org.jetbrains.jewel.intui.markdown.standalone.dark
import org.jetbrains.jewel.intui.markdown.standalone.light
import org.jetbrains.jewel.intui.markdown.standalone.styling.extensions.github.alerts.dark
import org.jetbrains.jewel.intui.markdown.standalone.styling.extensions.github.alerts.light
import org.jetbrains.jewel.intui.markdown.standalone.styling.extensions.github.tables.dark
import org.jetbrains.jewel.intui.markdown.standalone.styling.extensions.github.tables.light
import org.jetbrains.jewel.markdown.LazyMarkdown
import org.jetbrains.jewel.markdown.MarkdownBlock
import org.jetbrains.jewel.markdown.MarkdownMode
import org.jetbrains.jewel.markdown.extensions.autolink.AutolinkProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.alerts.AlertStyling
import org.jetbrains.jewel.markdown.extensions.github.alerts.GitHubAlertProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.alerts.GitHubAlertRendererExtension
import org.jetbrains.jewel.markdown.extensions.github.strikethrough.GitHubStrikethroughProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.strikethrough.GitHubStrikethroughRendererExtension
import org.jetbrains.jewel.markdown.extensions.github.tables.GfmTableStyling
import org.jetbrains.jewel.markdown.extensions.github.tables.GitHubTableProcessorExtension
import org.jetbrains.jewel.markdown.extensions.github.tables.GitHubTableRendererExtension
import org.jetbrains.jewel.markdown.extensions.images.Coil3ImageRendererExtension
import org.jetbrains.jewel.markdown.processing.MarkdownProcessor
import org.jetbrains.jewel.markdown.rendering.MarkdownBlockRenderer
import org.jetbrains.jewel.markdown.rendering.MarkdownStyling
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.scrollbarContentSafePadding

@OptIn(ExperimentalJewelApi::class)
@Composable
fun AdvancedMarkdownPreviewExample(rawMarkdown: CharSequence, modifier: Modifier = Modifier) {
    val isDark = JewelTheme.isDark
    val instanceUuid = JewelTheme.instanceUuid
    val uriHandler = LocalUriHandler.current
    val coilContext = LocalPlatformContext.current

    val markdownStyling = remember(instanceUuid) {
        if (isDark) MarkdownStyling.dark() else MarkdownStyling.light()
    }
    val processor = remember {
        MarkdownProcessor(
            extensions = listOf(
                AutolinkProcessorExtension,
                GitHubAlertProcessorExtension,
                GitHubStrikethroughProcessorExtension(),
                GitHubTableProcessorExtension,
            ),
            // EditorPreview is stateful; keep one processor instance per live preview surface.
            markdownMode = MarkdownMode.EditorPreview(null),
        )
    }
    val imageRendererExtension = remember(coilContext) {
        Coil3ImageRendererExtension.withDefaultLoader(coilContext)
    }

    var blocks by remember { mutableStateOf<List<MarkdownBlock>>(emptyList()) }

    LaunchedEffect(rawMarkdown) {
        blocks = withContext(Dispatchers.Default) {
            processor.processMarkdownDocument(rawMarkdown.toString())
        }
    }

    val blockRenderer = remember(markdownStyling, imageRendererExtension, isDark) {
        if (isDark) {
            MarkdownBlockRenderer.dark(
                styling = markdownStyling,
                rendererExtensions = listOf(
                    imageRendererExtension,
                    GitHubAlertRendererExtension(AlertStyling.dark(), markdownStyling),
                    GitHubStrikethroughRendererExtension,
                    GitHubTableRendererExtension(GfmTableStyling.dark(), markdownStyling),
                ),
            )
        } else {
            MarkdownBlockRenderer.light(
                styling = markdownStyling,
                rendererExtensions = listOf(
                    imageRendererExtension,
                    GitHubAlertRendererExtension(AlertStyling.light(), markdownStyling),
                    GitHubStrikethroughRendererExtension,
                    GitHubTableRendererExtension(GfmTableStyling.light(), markdownStyling),
                ),
            )
        }
    }

    val background = remember(instanceUuid) {
        if (isDark) Color(0xff0d1117) else Color.White
    }

    ProvideMarkdownStyling(markdownStyling, blockRenderer, NoOpCodeHighlighter) {
        val lazyListState = rememberLazyListState()

        VerticallyScrollableContainer(
            lazyListState as ScrollableState,
            modifier = modifier.background(background),
        ) {
            LazyMarkdown(
                blocks = blocks,
                modifier = Modifier.background(background),
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = 8.dp,
                    end = 8.dp + scrollbarContentSafePadding(),
                    bottom = 8.dp,
                ),
                state = lazyListState,
                selectable = true,
                onUrlClick = uriHandler::openUri,
            )
        }
    }
}
