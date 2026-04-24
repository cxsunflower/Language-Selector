package vegabobo.languageselector.ui.screen.about

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.BackButton
import vegabobo.languageselector.ui.components.Title
import vegabobo.languageselector.ui.screen.BaseScreen
import vegabobo.languageselector.ui.screen.main.getAppIcon
import vegabobo.languageselector.ui.theme.LanguageSelector
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.util.withContext
import vegabobo.languageselector.BuildConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navigateBack: () -> Unit
) {
    val libs = remember { mutableStateOf<Libs?>(null) }
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    libs.value = Libs.Builder().withContext(context).build()
    val libraries = libs.value!!.libraries

    AboutContent(
        navigateBack = navigateBack,
        appIcon = context.packageManager.getAppIcon(context.applicationInfo),
        appName = stringResource(R.string.app_name),
        versionText = stringResource(R.string.version).format(
            BuildConfig.VERSION_NAME,
            BuildConfig.VERSION_CODE
        ),
        githubTitle = stringResource(R.string.ghrepo),
        githubDescription = stringResource(R.string.view_source),
        libraries = libraries.map { library ->
            AboutLibraryItem(
                title = library.name,
                description = library.licenses.joinToString(separator = "") { it.name },
                url = library.website.orEmpty()
            )
        },
        onOpenGithub = {
            uriHandler.openUri("https://github.com/VegaBobo/Language-Selector")
        },
        onOpenLibrary = { url ->
            if (url.isNotEmpty()) {
                uriHandler.openUri(url)
            }
        }
    )
}

data class AboutLibraryItem(
    val title: String,
    val description: String,
    val url: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutContent(
    navigateBack: () -> Unit,
    appIcon: android.graphics.drawable.Drawable,
    appName: String,
    versionText: String,
    githubTitle: String,
    githubDescription: String,
    libraries: List<AboutLibraryItem>,
    onOpenGithub: () -> Unit,
    onOpenLibrary: (String) -> Unit,
) {
    val appIconBitmap = rememberDrawableBitmap(
        drawable = appIcon,
        size = 96.dp
    )

    BaseScreen(
        title = stringResource(R.string.about),
        navIcon = { BackButton { navigateBack() } }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(96.dp),
                        bitmap = appIconBitmap,
                        contentDescription = "App icon"
                    )
                    Text(text = appName, fontSize = 22.sp)
                    Text(versionText)
                }
            }
            item {
                Title(stringResource(id = R.string.app))
                PreferenceItem(
                    title = githubTitle,
                    description = githubDescription,
                    onClick = onOpenGithub
                )
            }
            item { Title(stringResource(R.string.deps_libs)) }
            items(libraries.size) {
                val thisLibrary = libraries[it]
                PreferenceItem(
                    title = thisLibrary.title,
                    description = thisLibrary.description,
                    onClick = { onOpenLibrary(thisLibrary.url) },
                )
            }
            item { Spacer(modifier = Modifier.padding(bottom = 16.dp)) }
        }
    }
}

@Composable
private fun rememberDrawableBitmap(
    drawable: Drawable,
    size: Dp,
): ImageBitmap {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx().coerceAtLeast(1) }

    return remember(drawable, sizePx) {
        // ColorDrawable 等预览图标没有固有尺寸，显式给尺寸避免 Preview 崩溃。
        drawable.toBitmap(width = sizePx, height = sizePx).asImageBitmap()
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AboutScreenPreview() {
    // 预览用静态依赖列表，覆盖应用信息与设置项排版。
    val previewLibraries = listOf(
        AboutLibraryItem("Shizuku", "Apache 2.0"),
        AboutLibraryItem("Hilt", "Apache 2.0"),
        AboutLibraryItem("Room", "Apache 2.0")
    )

    LanguageSelector(dynamicColor = false) {
        AboutContent(
            navigateBack = {},
            appIcon = ColorDrawable(Color(0xFF3B826A).toArgb()),
            appName = "Language Selector",
            versionText = "version 1.04 (5)",
            githubTitle = "GitHub repo",
            githubDescription = "View source code",
            libraries = previewLibraries,
            onOpenGithub = {},
            onOpenLibrary = {}
        )
    }
}

@Composable
fun PreferenceItem(
    title: String,
    description: String,
    icon: ImageVector? = null,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = 24.dp,
                top = 16.dp,
                bottom = 16.dp,
                end = 16.dp
            )
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
            )
        }
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
