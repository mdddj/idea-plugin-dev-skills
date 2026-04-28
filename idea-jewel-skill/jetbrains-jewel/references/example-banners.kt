import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultInformationBanner
import org.jetbrains.jewel.ui.component.InlineWarningBanner

@Composable
fun BannersExample(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DefaultInformationBanner(
            style = JewelTheme.defaultBannerStyle.information,
            text = "A new update is available for this workspace.",
        )

        InlineWarningBanner(
            style = JewelTheme.inlineBannerStyle.warning,
            text = "Configuration is incomplete. Review project settings.",
        )
    }
}
