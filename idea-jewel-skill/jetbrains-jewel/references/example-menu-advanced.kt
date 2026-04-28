import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.PopupMenu
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.separator

@Composable
fun AdvancedMenuExample() {
    var showMenu by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf("Standard") }

    Box {
        DefaultButton(onClick = { showMenu = true }) {
            Text("Advanced menu")
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
                        text = "Tip: use keyboard shortcuts to switch faster.",
                        style = JewelTheme.typography.small,
                        color = JewelTheme.globalColors.text.info,
                    )
                },
            ) {
                selectableItem(selected = mode == "Beginner", onClick = { mode = "Beginner" }) { Text("Beginner") }
                selectableItem(selected = mode == "Standard", onClick = { mode = "Standard" }) { Text("Standard") }
                separator()
                submenu(
                    submenu = {
                        selectableItem(selected = mode == "Advanced", onClick = { mode = "Advanced" }) {
                            Text("Advanced")
                        }
                        selectableItem(selected = mode == "Expert", onClick = { mode = "Expert" }) {
                            Text("Expert")
                        }
                    },
                    content = { Text("More modes") },
                )
            }
        }
    }
}
