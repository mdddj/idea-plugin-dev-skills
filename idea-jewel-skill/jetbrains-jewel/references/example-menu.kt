import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.PopupMenu
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.separator
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun MenuExample() {
    var showMenu by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("Copy") }

    Box {
        DefaultButton(onClick = { showMenu = true }) {
            Text("Show menu")
        }

        if (showMenu) {
            PopupMenu(
                onDismissRequest = {
                    showMenu = false
                    true
                },
                horizontalAlignment = Alignment.Start,
            ) {
                selectableItem(
                    selected = selectedAction == "Copy",
                    onClick = { selectedAction = "Copy" },
                    iconKey = AllIconsKeys.Actions.Copy,
                ) {
                    Text("Copy")
                }
                selectableItem(
                    selected = selectedAction == "Paste",
                    onClick = { selectedAction = "Paste" },
                    iconKey = AllIconsKeys.Actions.MenuPaste,
                ) {
                    Text("Paste")
                }
                separator()
                submenu(
                    submenu = {
                        selectableItem(
                            selected = selectedAction == "Advanced",
                            onClick = { selectedAction = "Advanced" },
                        ) {
                            Text("Advanced option")
                        }
                    },
                    content = { Text("More options") },
                )
            }
        }
    }
}
