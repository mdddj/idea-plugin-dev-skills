import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import org.jetbrains.jewel.bridge.addComposeTab
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

class JewelToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // addComposeTab() already enables the new Swing compositing mode and hosts content in JewelComposePanel.
        // That means the bridge theme is already applied here; do not wrap another SwingBridgeTheme around it.
        toolWindow.addComposeTab(tabDisplayName = "Jewel") {
            var refreshCount by remember { mutableIntStateOf(0) }

            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Project: ${project.name}")
                Text("Tool window: ${toolWindow.id}")
                Text("Refresh count: $refreshCount")

                DefaultButton(onClick = { refreshCount += 1 }) {
                    Text("Refresh Compose content")
                }
            }
        }
    }
}
