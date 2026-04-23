package vegabobo.languageselector.ui.screen.appinfo

import android.graphics.drawable.ColorDrawable
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.BackButton
import vegabobo.languageselector.ui.components.LocaleItemList
import vegabobo.languageselector.ui.components.QuickTextButton
import vegabobo.languageselector.ui.components.Title
import vegabobo.languageselector.ui.screen.BaseScreen
import vegabobo.languageselector.ui.theme.LanguageSelector
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoScreen(
    appId: String,
    navigateBack: () -> Unit,
    appInfoVm: AppInfoVm = hiltViewModel(),
) {
    val uiState by appInfoVm.uiState.collectAsState()
    val ctx = LocalContext.current
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun pinToast(locale: String) {
        val pinTxt =
            ctx.resources.getString(R.string.pinned_ok).format(locale)
        Toast.makeText(ctx, pinTxt, Toast.LENGTH_SHORT).show()
    }

    fun unpinToast(locale: String) {
        val pinTxt =
            ctx.resources.getString(R.string.unpinned).format(locale)
        Toast.makeText(ctx, pinTxt, Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        appInfoVm.initFromAppId(appId)
        appInfoVm.updatePinnedLangsFromSP()
    }
    AppInfoContent(
        uiState = uiState,
        navigateBack = navigateBack,
        onClickOpen = { appInfoVm.onClickOpen() },
        onClickForceClose = { appInfoVm.onClickForceClose() },
        onClickSettings = { appInfoVm.onClickSettings() },
        onClickLocale = { appInfoVm.onClickLocale(it) },
        onClickResetLang = { appInfoVm.onClickResetLang() },
        onClickSingleLanguage = { appInfoVm.onClickSingleLanguage(it) },
        onPinLang = { appInfoVm.onPinLang(it) },
        onRemovePin = { appInfoVm.onRemovePin(it) },
        onBackWhenSelectedLang = { appInfoVm.onBackWhenSelectedLang() },
        onPinnedFeedback = { pinToast(it) },
        onUnpinnedFeedback = { unpinToast(it) },
        listState = listState,
        coroutineScope = coroutineScope
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInfoContent(
    uiState: AppInfoState,
    navigateBack: () -> Unit,
    onClickOpen: () -> Unit,
    onClickForceClose: () -> Unit,
    onClickSettings: () -> Unit,
    onClickLocale: (SingleLocale) -> Unit,
    onClickResetLang: () -> Unit,
    onClickSingleLanguage: (Int) -> Unit,
    onPinLang: (SingleLocale) -> Unit,
    onRemovePin: (SingleLocale) -> Unit,
    onBackWhenSelectedLang: () -> Unit,
    onPinnedFeedback: (String) -> Unit,
    onUnpinnedFeedback: (String) -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState = rememberLazyListState(),
    coroutineScope: kotlinx.coroutines.CoroutineScope = rememberCoroutineScope(),
) {
    val appIconBitmap = rememberAppIconBitmap(
        drawable = uiState.appIcon,
        placeholderResId = R.drawable.icon_placeholder,
        size = 84.dp
    )

    BaseScreen(
        title = stringResource(R.string.app_language),
        navIcon = {
            BackButton { navigateBack() }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .animateContentSize(),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 18.dp, end = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(84.dp),
                        bitmap = appIconBitmap,
                        contentDescription = "App icon"
                    )
                    Column(
                        modifier = Modifier
                            .padding(18.dp)
                            .weight(1f)
                    ) {
                        Text(text = uiState.appName, fontSize = 22.sp, maxLines = 1)
                        Text(text = uiState.appPackage, fontSize = 14.sp, maxLines = 1)
                        Text(
                            text = uiState.currentLanguage.ifEmpty {
                                stringResource(R.string.system_default)
                            },
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    QuickTextButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClickOpen,
                        icon = Icons.AutoMirrored.Outlined.OpenInNew,
                        text = stringResource(R.string.open)
                    )
                    QuickTextButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClickForceClose,
                        icon = Icons.Outlined.Close,
                        text = stringResource(R.string.close)
                    )
                    QuickTextButton(
                        modifier = Modifier.weight(1f),
                        onClick = onClickSettings,
                        icon = Icons.Outlined.Settings,
                        text = stringResource(R.string.settings)
                    )
                }
            }

            if (uiState.selectedLanguage != -1) {
                item { Title(stringResource(R.string.region)) }
                items(uiState.listOfAllLanguages[uiState.selectedLanguage].locales.size) { index ->
                    val thisLangReg =
                        uiState.listOfAllLanguages[uiState.selectedLanguage].locales[index]
                    LocaleItemList(
                        itemText = thisLangReg.name,
                        onClick = {
                            onClickLocale(thisLangReg)
                            onBackWhenSelectedLang()
                            coroutineScope.launch { listState.scrollToItem(0) }
                        },
                        onLongClick = {
                            onPinnedFeedback(thisLangReg.name)
                            onPinLang(thisLangReg)
                        }
                    )
                }
            } else {
                if (uiState.listOfPinnedLanguages.isNotEmpty()) {
                    item { Title(stringResource(R.string.pinned)) }
                    items(uiState.listOfPinnedLanguages.size) { index ->
                        val thisLanguage = uiState.listOfPinnedLanguages[index]
                        LocaleItemList(
                            itemText = thisLanguage.name,
                            onClick = { onClickLocale(thisLanguage) },
                            onLongClick = {
                                onUnpinnedFeedback(thisLanguage.name)
                                onRemovePin(thisLanguage)
                            }
                        )
                    }
                }

                item { Title(stringResource(R.string.user_languages)) }
                item {
                    LocaleItemList(stringResource(R.string.system_default), onClick = onClickResetLang)
                }
                items(uiState.listOfSuggestedLanguages.size) { index ->
                    val thisLanguage = uiState.listOfSuggestedLanguages[index]
                    LocaleItemList(
                        itemText = thisLanguage.name,
                        onClick = { onClickLocale(thisLanguage) },
                        onLongClick = {
                            onPinnedFeedback(thisLanguage.name)
                            onPinLang(thisLanguage)
                        }
                    )
                }

                item { Title(stringResource(R.string.all_languages)) }
                items(uiState.listOfAllLanguages.size) { index ->
                    val thisLanguage = uiState.listOfAllLanguages[index]
                    LocaleItemList(thisLanguage.language) {
                        onClickSingleLanguage(index)
                        coroutineScope.launch { listState.scrollToItem(0) }
                    }
                }
            }
            item { Spacer(modifier = Modifier.padding(bottom = 16.dp)) }
        }
    }

    if (uiState.selectedLanguage != -1) {
        BackHandler { onBackWhenSelectedLang() }
    }
}

@Composable
private fun rememberAppIconBitmap(
    drawable: Drawable?,
    placeholderResId: Int,
    size: Dp,
): ImageBitmap {
    val ctx = LocalContext.current
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx().coerceAtLeast(1) }

    return remember(drawable, placeholderResId, sizePx, ctx) {
        // 显式传入位图尺寸，避免 ColorDrawable 等无固有宽高的图标在预览中崩溃。
        val safeBitmap =
            drawable?.let {
                runCatching {
                    it.toBitmap(width = sizePx, height = sizePx)
                }.getOrNull()
            } ?: BitmapFactory.decodeResource(ctx.resources, placeholderResId)

        safeBitmap.asImageBitmap()
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AppInfoScreenPreview() {
    // 预览用静态状态覆盖详情页的主要信息层次。
    val previewPinned = remember {
        // 预览态数据需要通过 remember 持有，避免每次重组重新创建状态对象。
        mutableStateListOf(
            SingleLocale("English (United States)", "en-US"),
            SingleLocale("Português (Brasil)", "pt-BR")
        )
    }
    val previewSuggested = remember {
        mutableStateListOf(
            SingleLocale("English (United States)", "en-US"),
            SingleLocale("日本語", "ja-JP")
        )
    }
    val previewAll = remember {
        mutableStateListOf(
            LocaleRegion(
                language = "English",
                locales = arrayListOf(
                    SingleLocale("English (United States)", "en-US"),
                    SingleLocale("English (United Kingdom)", "en-GB")
                )
            ),
            LocaleRegion(
                language = "Português",
                locales = arrayListOf(
                    SingleLocale("Português (Brasil)", "pt-BR")
                )
            )
        )
    }

    LanguageSelector(dynamicColor = false) {
        AppInfoContent(
            uiState = AppInfoState(
                appIcon = ColorDrawable(Color(0xFF4C8F82).toArgb()),
                appName = "Instagram",
                appPackage = "com.instagram.android",
                currentLanguage = "English (United States)",
                listOfSuggestedLanguages = previewSuggested,
                listOfPinnedLanguages = previewPinned,
                listOfAllLanguages = previewAll
            ),
            navigateBack = {},
            onClickOpen = {},
            onClickForceClose = {},
            onClickSettings = {},
            onClickLocale = {},
            onClickResetLang = {},
            onClickSingleLanguage = {},
            onPinLang = {},
            onRemovePin = {},
            onBackWhenSelectedLang = {},
            onPinnedFeedback = {},
            onUnpinnedFeedback = {}
        )
    }
}
