package vegabobo.languageselector.ui.screen.main

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.AppListItem
import vegabobo.languageselector.ui.components.AppSearchBar
import vegabobo.languageselector.ui.screen.BaseScreen
import vegabobo.languageselector.ui.theme.LanguageSelector

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    mainScreenVm: MainScreenVm = hiltViewModel(),
    navigateToAppScreen: (String) -> Unit,
    navigateToAbout: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
) {
    val uiState by mainScreenVm.uiState.collectAsState()
    val sb = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        mainScreenVm.reloadLastSelectedItem()
        mainScreenVm.uiState.collectLatest {
            when (it.snackBarDisplay) {
                SnackBarDisplay.MOVED_TO_TOP -> {
                    val r = sb.showSnackbar(
                        message = "Modified app has been moved up",
                        actionLabel = "Navigate"
                    )
                    if (r == SnackbarResult.ActionPerformed) {
                        val i =
                            mainScreenVm.getIndexFromAppInfoItem() + 1 /* first item is a spacer */
                        lazyListState.animateScrollToItem(i)
                    }
                }

                SnackBarDisplay.MOVED_TO_BOTTOM -> {
                    val r = sb.showSnackbar(
                        message = "Unmodified has been moved down",
                        actionLabel = "Navigate"
                    )
                    if (r == SnackbarResult.ActionPerformed) {
                        val i =
                            mainScreenVm.getIndexFromAppInfoItem() + 1 /* first item is a spacer */
                        lazyListState.animateScrollToItem(i)
                    }
                }

                else -> {}
            }
            mainScreenVm.resetSnackBarDisplay()
        }
    }
    MainScreenContent(
        uiState = uiState,
        snackBarHostState = sb,
        lazyListState = lazyListState,
        navigateToAppScreen = navigateToAppScreen,
        navigateToAbout = navigateToAbout,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        onSearchTextFieldChange = { mainScreenVm.onSearchTextFieldChange(it) },
        onClickApp = { mainScreenVm.onClickApp(it) },
        onToggleUserAppsVisibility = { mainScreenVm.toggleUserAppsVisibility() },
        onToggleSystemAppsVisibility = { mainScreenVm.toggleSystemAppsVisibility() },
        onClickProceedShizuku = { mainScreenVm.onClickProceedShizuku() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    uiState: MainScreenState,
    navigateToAppScreen: (String) -> Unit,
    navigateToAbout: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSearchTextFieldChange: (String) -> Unit,
    onClickApp: (AppInfo) -> Unit,
    onToggleUserAppsVisibility: () -> Unit,
    onToggleSystemAppsVisibility: () -> Unit,
    onClickProceedShizuku: () -> Unit,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    lazyListState: androidx.compose.foundation.lazy.LazyListState = rememberLazyListState(),
) {
    BaseScreen(
        title = stringResource(R.string.app_name),
        snackBarHost = snackBarHostState,
        actions = {
            val themeToggleDescription = stringResource(
                if (isDarkTheme) R.string.switch_to_light_theme else R.string.switch_to_dark_theme
            )
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = themeToggleDescription
                )
            }
            TextButton(onClick = navigateToAbout) {
                Text(text = stringResource(R.string.about))
            }
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .semantics { isTraversalGroup = true }
                ) {
                    AppSearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { traversalIndex = 0f },
                        placeholder = stringResource(R.string.search),
                        onUpdatedValue = onSearchTextFieldChange,
                        query = uiState.searchTextFieldValue,
                    )
                    SearchBarActions(
                        modifier = Modifier.semantics { traversalIndex = 0.5f },
                        isShowingUserApps = uiState.isShowUserAppsHome,
                        isShowingSystemApps = uiState.isShowSystemAppsHome,
                        onClickToggleUserApps = onToggleUserAppsVisibility,
                        onClickToggleSystemApps = onToggleSystemAppsVisibility,
                    )
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .weight(1f)
                            .semantics { traversalIndex = 1f }
                    ) {
                        items(uiState.listOfApps.size) {
                            val thisApp = uiState.listOfApps[it]
                            if (!isAppTypeVisible(uiState, thisApp)) {
                                return@items
                            }
                            if (!matchesSearchQuery(uiState.searchTextFieldValue, thisApp)) {
                                return@items
                            }
                            AppListItem(
                                modifier = Modifier.padding(
                                    start = 26.dp,
                                    end = 26.dp,
                                    top = 4.dp,
                                    bottom = 4.dp
                                ),
                                app = thisApp,
                                onClickApp = { pkg ->
                                    onClickApp(thisApp)
                                    navigateToAppScreen(pkg)
                                }
                            )
                        }
                    }
                }

                if (uiState.operationMode == OperationMode.NONE) {
                    ShizukuRequiredWarning(onClickContinue = onClickProceedShizuku)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MainScreenPreview() {
    // 预览使用静态数据，避免依赖 Hilt 和系统应用列表。
    val previewApps = remember {
        // 预览态列表由 remember 托管，避免组合过程中重复创建可观察状态。
        mutableStateListOf(
            AppInfo(
                icon = ColorDrawable(Color(0xFF6C8E5E).toArgb()),
                name = "Instagram",
                pkg = "com.instagram.android",
                labels = listOf(AppLabels.MODIFIED)
            ),
            AppInfo(
                icon = ColorDrawable(Color(0xFFE35D3E).toArgb()),
                name = "YouTube",
                pkg = "com.google.android.youtube"
            ),
            AppInfo(
                icon = ColorDrawable(Color(0xFF546E7A).toArgb()),
                name = "Settings",
                pkg = "com.android.settings",
                labels = listOf(AppLabels.SYSTEM_APP)
            )
        )
    }
    LanguageSelector(dynamicColor = false) {
        MainScreenContent(
            uiState = MainScreenState(
                listOfApps = previewApps,
                operationMode = OperationMode.SHIZUKU,
                isLoading = false,
                isShowUserAppsHome = true,
                isShowSystemAppsHome = true
            ),
            navigateToAppScreen = {},
            navigateToAbout = {},
            isDarkTheme = false,
            onToggleTheme = {},
            onSearchTextFieldChange = {},
            onClickApp = {},
            onToggleUserAppsVisibility = {},
            onToggleSystemAppsVisibility = {},
            onClickProceedShizuku = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun MainScreenPermissionPreview() {
    val previewApps = remember { mutableStateListOf<AppInfo>() }

    LanguageSelector(darkTheme = true, dynamicColor = false) {
        MainScreenContent(
            uiState = MainScreenState(
                listOfApps = previewApps,
                operationMode = OperationMode.NONE,
                isLoading = false
            ),
            navigateToAppScreen = {},
            navigateToAbout = {},
            isDarkTheme = true,
            onToggleTheme = {},
            onSearchTextFieldChange = {},
            onClickApp = {},
            onToggleUserAppsVisibility = {},
            onToggleSystemAppsVisibility = {},
            onClickProceedShizuku = {}
        )
    }
}

private fun isAppTypeVisible(uiState: MainScreenState, app: AppInfo): Boolean {
    return if (app.isSystemApp()) {
        uiState.isShowSystemAppsHome
    } else {
        uiState.isShowUserAppsHome
    }
}

private fun matchesSearchQuery(query: String, app: AppInfo): Boolean {
    if (query.isBlank()) {
        return true
    }

    val normalizedQuery = query.lowercase()
    return app.pkg.lowercase().contains(normalizedQuery) ||
        app.name.lowercase().contains(normalizedQuery)
}
