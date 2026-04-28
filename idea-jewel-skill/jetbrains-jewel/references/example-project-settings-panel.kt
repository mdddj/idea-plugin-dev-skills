import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.ListComboBox
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Text
import javax.swing.JComponent

data class JewelWorkspaceUiState(
    val previewLayout: String = "Split",
    val autoScrollPreview: Boolean = true,
    val pinToolWindow: Boolean = false,
)

@Service(Service.Level.PROJECT)
@State(
    name = "JewelWorkspaceSettings",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)],
)
class JewelWorkspaceSettingsService(project: Project) :
    SimplePersistentStateComponent<JewelWorkspaceSettingsService.State>(State()) {
    class State : BaseState() {
        var previewLayout by string("Split")
        var autoScrollPreview by property(true)
        var pinToolWindow by property(false)
    }

    fun snapshot(): JewelWorkspaceUiState {
        return JewelWorkspaceUiState(
            previewLayout = state.previewLayout ?: "Split",
            autoScrollPreview = state.autoScrollPreview,
            pinToolWindow = state.pinToolWindow,
        )
    }

    fun loadFrom(uiState: JewelWorkspaceUiState) {
        state.previewLayout = uiState.previewLayout
        state.autoScrollPreview = uiState.autoScrollPreview
        state.pinToolWindow = uiState.pinToolWindow
    }

    companion object {
        fun getInstance(project: Project): JewelWorkspaceSettingsService = project.service()
    }
}

fun createJewelWorkspaceSettingsPanel(project: Project): JComponent = JewelComposePanel {
    val settings = remember(project) { JewelWorkspaceSettingsService.getInstance(project) }
    var uiState by remember { mutableStateOf(settings.snapshot()) }

    JewelWorkspaceSettingsPanel(
        projectName = project.name,
        state = uiState,
        onStateChange = { uiState = it },
        onApply = { settings.loadFrom(uiState) },
        onReset = { uiState = settings.snapshot() },
    )
}

private val jewelPreviewLayouts = listOf("Split", "Editor only", "Preview only")

@Composable
private fun JewelWorkspaceSettingsPanel(
    projectName: String,
    state: JewelWorkspaceUiState,
    onStateChange: (JewelWorkspaceUiState) -> Unit,
    onApply: () -> Unit,
    onReset: () -> Unit,
) {
    Column(
        modifier = Modifier
            .widthIn(max = 460.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Workspace settings for $projectName")
        Text("This variant stores values in the project workspace file.")

        ListComboBox(
            items = jewelPreviewLayouts,
            selectedIndex = jewelPreviewLayouts.indexOf(state.previewLayout).coerceAtLeast(0),
            onSelectedItemChange = { index ->
                onStateChange(state.copy(previewLayout = jewelPreviewLayouts[index]))
            },
            modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth(),
            itemKeys = { _, item -> item },
        )

        CheckboxRow(
            text = "Auto-scroll preview with the editor",
            checked = state.autoScrollPreview,
            onCheckedChange = {
                onStateChange(state.copy(autoScrollPreview = it))
            },
        )

        CheckboxRow(
            text = "Pin the tool window for this project",
            checked = state.pinToolWindow,
            onCheckedChange = {
                onStateChange(state.copy(pinToolWindow = it))
            },
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DefaultButton(onClick = onApply) {
                Text("Apply to workspace")
            }

            OutlinedButton(onClick = onReset) {
                Text("Reload workspace values")
            }
        }
    }
}
