package vegabobo.languageselector.ui.components

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.screen.main.AppLabels
import vegabobo.languageselector.ui.screen.main.AppInfo
import vegabobo.languageselector.ui.theme.LanguageSelector

@Composable
fun AppListItem(
    modifier: Modifier = Modifier,
    app: AppInfo,
    onClickApp: (String) -> Unit
) {
    val density = LocalDensity.current
    val iconSizePx = with(density) { 32.dp.roundToPx().coerceAtLeast(1) }
    val iconBitmap = remember(app.icon, iconSizePx) {
        // 显式传入位图尺寸，避免 Preview 中的 ColorDrawable 因无固有宽高而崩溃。
        app.icon.toBitmap(width = iconSizePx, height = iconSizePx).asImageBitmap()
    }

    Row(
        modifier = Modifier
            .clickable { onClickApp(app.pkg) }
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(32.dp),
            bitmap = iconBitmap,
            contentDescription = "app icon"
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy((-4).dp)
        ) {
            Text(text = app.name, fontSize = 18.sp, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(text = app.pkg, fontSize = 12.sp, maxLines = 1)
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (app.isSystemApp()) {
                    TextLabel(
                        text = stringResource(R.string.system_app),
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                } else {
                    TextLabel(
                        text = stringResource(R.string.user_app),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                if (app.isModified()) {
                    TextLabel(
                        text = stringResource(R.string.modified),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun TextLabel(
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    // 使用 Material 3 胶囊样式，让标签在不同主题下保持清晰。
    Surface(
        shape = RoundedCornerShape(50),
        color = containerColor,
        contentColor = contentColor,
        border = BorderStroke(1.dp, contentColor.copy(alpha = 0.16f))
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            text = text,
            maxLines = 1,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun AppListItemPreview() {
    LanguageSelector(dynamicColor = false) {
        // 预览同时覆盖用户应用、系统应用和已修改状态。
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AppListItem(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                app = AppInfo(
                    icon = ColorDrawable(Color(0xFF4C8F82).toArgb()),
                    name = "Language Selector",
                    pkg = "vegabobo.languageselector",
                    labels = listOf(AppLabels.MODIFIED)
                ),
                onClickApp = {}
            )
            AppListItem(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                app = AppInfo(
                    icon = ColorDrawable(Color(0xFFB16A42).toArgb()),
                    name = "Android System",
                    pkg = "android",
                    labels = listOf(AppLabels.SYSTEM_APP)
                ),
                onClickApp = {}
            )
        }
    }
}
