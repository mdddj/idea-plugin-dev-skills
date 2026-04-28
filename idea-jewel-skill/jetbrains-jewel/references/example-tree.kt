import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.lazy.tree.buildTree
import org.jetbrains.jewel.foundation.lazy.tree.rememberTreeState
import org.jetbrains.jewel.ui.component.SpeedSearchArea
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.search.SpeedSearchableTree

@Composable
fun TreeExample(modifier: Modifier = Modifier) {
    val tree =
        buildTree {
            addNode("src") {
                addLeaf("main.kt")
                addLeaf("app.kt")
            }
            addNode("resources") {
                addLeaf("logo.svg")
            }
        }
    val treeState = rememberTreeState()

    SpeedSearchArea(modifier) {
        SpeedSearchableTree(
            tree = tree,
            treeState = treeState,
            modifier = Modifier.size(220.dp, 180.dp).focusable(),
            onElementClick = {},
            onElementDoubleClick = {},
            nodeText = { it.data },
        ) { element ->
            Text(element.data)
        }
    }
}
