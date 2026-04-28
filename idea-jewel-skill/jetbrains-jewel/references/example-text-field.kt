import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextField
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun SearchFieldExample(modifier: Modifier = Modifier) {
    val state = rememberTextFieldState("")

    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        TextField(
            state = state,
            modifier = Modifier.width(260.dp),
            placeholder = { Text("Search") },
            leadingIcon = {
                Icon(
                    key = AllIconsKeys.Actions.Find,
                    contentDescription = "Search",
                )
            },
            trailingIcon = {
                if (state.text.isNotEmpty()) {
                    IconButton(onClick = { state.setTextAndPlaceCursorAtEnd("") }) {
                        Icon(
                            key = AllIconsKeys.General.Close,
                            contentDescription = "Clear",
                        )
                    }
                }
            },
        )
    }
}
