import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.bridge.JewelComposePanel
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.PopupMenu
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.separator
import javax.swing.JComponent

fun createBridgePopupDemoPanel(): JComponent = JewelComposePanel {
    BridgePopupPanelExample()
}

@Composable
fun BridgePopupPanelExample() {
    var showMenu by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("Run inspection") }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth().padding(16.dp),
    ) {
        Text("Last action: $selectedAction")

        Box {
            DefaultButton(onClick = { showMenu = true }) {
                Text("Open bridge popup")
            }

            if (showMenu) {
                PopupMenu(
                    onDismissRequest = {
                        showMenu = false
                        true
                    },
                    horizontalAlignment = Alignment.Start,
                    adContent = {
                        Text(
                            text = "Inside JewelComposePanel this popup is rendered through JBPopup.",
                            style = JewelTheme.typography.small,
                            color = JewelTheme.globalColors.text.info,
                        )
                    },
                ) {
                    selectableItem(
                        selected = selectedAction == "Run inspection",
                        onClick = {
                            selectedAction = "Run inspection"
                            showMenu = false
                        },
                    ) {
                        Text("Run inspection")
                    }
                    selectableItem(
                        selected = selectedAction == "Pin results",
                        onClick = {
                            selectedAction = "Pin results"
                            showMenu = false
                        },
                    ) {
                        Text("Pin results")
                    }
                    separator()
                    selectableItem(
                        selected = selectedAction == "Export report",
                        onClick = {
                            selectedAction = "Export report"
                            showMenu = false
                        },
                    ) {
                        Text("Export report")
                    }
                }
            }
        }
    }
}
