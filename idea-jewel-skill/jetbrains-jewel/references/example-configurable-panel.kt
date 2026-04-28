import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SettingsCategory
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.ListComboBox
import org.jetbrains.jewel.ui.component.Text
import javax.swing.JComponent

data class JewelSettingsUiState(
    val themeVariant: String = "Follow IDE",
    val enableMarkdownPreview: Boolean = true,
    val compactToolWindow: Boolean = false,
)

@Service(Service.Level.APP)
@State(
    name = "JewelDemoSettings",
    storages = [Storage("jewel-demo.xml")],
    category = SettingsCategory.TOOLS,
)
// For project-specific settings, switch this service to Service.Level.PROJECT
// and use a workspace/project storage instead of an app-level XML file.
class JewelSettingsService : SimplePersistentStateComponent<JewelSettingsService.State>(State()) {
    class State : BaseState() {
        var themeVariant by string("Follow IDE")
        var enableMarkdownPreview by property(true)
        var compactToolWindow by property(false)
    }

    fun snapshot(): JewelSettingsUiState {
        return JewelSettingsUiState(
            themeVariant = state.themeVariant ?: "Follow IDE",
            enableMarkdownPreview = state.enableMarkdownPreview,
            compactToolWindow = state.compactToolWindow,
        )
    }

    fun loadFrom(uiState: JewelSettingsUiState) {
        state.themeVariant = uiState.themeVariant
        state.enableMarkdownPreview = uiState.enableMarkdownPreview
        state.compactToolWindow = uiState.compactToolWindow
    }

    companion object {
        fun getInstance(): JewelSettingsService = service()
    }
}

class JewelSettingsConfigurable(
    private val settings: JewelSettingsService = JewelSettingsService.getInstance(),
) : SearchableConfigurable, Configurable.NoScroll {
    private var uiState by mutableStateOf(settings.snapshot())
    private var component: JComponent? = null

    override fun getId(): String = "example.jewel.settings"

    override fun getDisplayName(): String = "Jewel Demo"

    override fun createComponent(): JComponent {
        return component ?: JewelComposePanel {
            // JewelComposePanel already provides SwingBridgeTheme and bridge popup rendering.
            JewelSettingsPanel(
                state = uiState,
                onStateChange = { uiState = it },
            )
        }.also { component = it }
    }

    override fun isModified(): Boolean = uiState != settings.snapshot()

    override fun apply() {
        settings.loadFrom(uiState)
    }

    override fun reset() {
        uiState = settings.snapshot()
    }

    override fun disposeUIResources() {
        component = null
    }
}

private val jewelThemeVariants = listOf("Follow IDE", "Light", "Dark")

@Composable
private fun JewelSettingsPanel(
    state: JewelSettingsUiState,
    onStateChange: (JewelSettingsUiState) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Jewel plugin settings")

        ListComboBox(
            items = jewelThemeVariants,
            selectedIndex = jewelThemeVariants.indexOf(state.themeVariant).coerceAtLeast(0),
            onSelectedItemChange = { index ->
                onStateChange(state.copy(themeVariant = jewelThemeVariants[index]))
            },
            modifier = Modifier.widthIn(max = 280.dp).fillMaxWidth(),
            itemKeys = { _, item -> item },
        )

        CheckboxRow(
            text = "Enable Markdown preview",
            checked = state.enableMarkdownPreview,
            onCheckedChange = {
                onStateChange(state.copy(enableMarkdownPreview = it))
            },
        )

        CheckboxRow(
            text = "Use compact tool window layout",
            checked = state.compactToolWindow,
            onCheckedChange = {
                onStateChange(state.copy(compactToolWindow = it))
            },
        )
    }
}
