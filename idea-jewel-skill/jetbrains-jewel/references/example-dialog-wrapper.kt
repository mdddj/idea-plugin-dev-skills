import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.ListComboBox
import org.jetbrains.jewel.ui.component.Text
import javax.swing.JComponent

data class JewelReviewDialogResult(
    val scope: String,
    val pinToolWindow: Boolean,
    val openMarkdownPreview: Boolean,
)

class JewelReviewDialog(private val project: Project) : DialogWrapper(project) {
    private var scope by mutableStateOf("Current file")
    private var pinToolWindow by mutableStateOf(true)
    private var openMarkdownPreview by mutableStateOf(false)

    private val composePanel = JewelComposePanel {
        JewelReviewDialogPanel(
            projectName = project.name,
            scope = scope,
            onScopeChange = { scope = it },
            pinToolWindow = pinToolWindow,
            onPinToolWindowChange = { pinToolWindow = it },
            openMarkdownPreview = openMarkdownPreview,
            onOpenMarkdownPreviewChange = { openMarkdownPreview = it },
        )
    }

    init {
        title = "Jewel Review"
        setOKButtonText("Run Review")
        init()
    }

    override fun createCenterPanel(): JComponent = composePanel

    override fun getPreferredFocusedComponent(): JComponent = composePanel

    fun result(): JewelReviewDialogResult {
        return JewelReviewDialogResult(
            scope = scope,
            pinToolWindow = pinToolWindow,
            openMarkdownPreview = openMarkdownPreview,
        )
    }
}

fun showJewelReviewDialog(project: Project): JewelReviewDialogResult? {
    val dialog = JewelReviewDialog(project)
    return if (dialog.showAndGet()) dialog.result() else null
}

private val jewelReviewScopes = listOf("Current file", "Directory", "Project")

@Composable
private fun JewelReviewDialogPanel(
    projectName: String,
    scope: String,
    onScopeChange: (String) -> Unit,
    pinToolWindow: Boolean,
    onPinToolWindowChange: (Boolean) -> Unit,
    openMarkdownPreview: Boolean,
    onOpenMarkdownPreviewChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier
            .widthIn(min = 420.dp, max = 520.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Run a Jewel-backed review inside a modal IntelliJ dialog.")
        Text("Project: $projectName")

        ListComboBox(
            items = jewelReviewScopes,
            selectedIndex = jewelReviewScopes.indexOf(scope).coerceAtLeast(0),
            onSelectedItemChange = { index ->
                onScopeChange(jewelReviewScopes[index])
            },
            modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth(),
            itemKeys = { _, item -> item },
        )

        CheckboxRow(
            text = "Pin the tool window after the review completes",
            checked = pinToolWindow,
            onCheckedChange = onPinToolWindowChange,
        )

        CheckboxRow(
            text = "Open Markdown preview for the generated report",
            checked = openMarkdownPreview,
            onCheckedChange = onOpenMarkdownPreviewChange,
        )
    }
}
