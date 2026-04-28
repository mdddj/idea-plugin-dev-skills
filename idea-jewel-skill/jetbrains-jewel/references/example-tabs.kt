import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.SimpleTabContent
import org.jetbrains.jewel.ui.component.TabData
import org.jetbrains.jewel.ui.component.TabStrip
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.theme.defaultTabStyle

@Composable
fun TabsExample() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var tabIds by remember { mutableStateOf(listOf(1, 2, 3)) }

    val tabs = tabIds.mapIndexed { index, id ->
        TabData.Default(
            selected = index == selectedIndex,
            content = { state ->
                SimpleTabContent(
                    label = "Tab $id",
                    state = state,
                )
            },
            onClick = { selectedIndex = index },
            onClose = {
                val next = tabIds.toMutableList().also { it.removeAt(index) }
                tabIds = if (next.isEmpty()) listOf(1) else next
                selectedIndex = selectedIndex.coerceAtMost(tabIds.lastIndex)
            },
        )
    }

    Row {
        TabStrip(
            tabs = tabs,
            style = JewelTheme.defaultTabStyle,
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = {
                val nextId = (tabIds.maxOrNull() ?: 0) + 1
                tabIds = tabIds + nextId
                selectedIndex = tabIds.lastIndex
            },
            modifier = Modifier.size(JewelTheme.defaultTabStyle.metrics.tabHeight),
        ) {
            Icon(
                key = AllIconsKeys.General.Add,
                contentDescription = "Add tab",
            )
        }
    }
}
