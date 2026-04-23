package vegabobo.languageselector.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    snackBarHost: SnackbarHostState? = null,
    topBar: (@Composable (TopAppBarScrollBehavior) -> Unit)? = null,
    navIcon: (@Composable () -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    // 统一由 BaseScreen 托管 Scaffold，避免各页面重复处理基础容器状态。
    val snackbarHostState = snackBarHost ?: remember { SnackbarHostState() }
    val defScrollBehavior = topBar != null || title?.isNotEmpty() == true
    val scrollBehavior =
        if (defScrollBehavior)
            TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
        else
            null
    val scaffoldModifier =
        if (scrollBehavior != null)
            Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
        else
            Modifier

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .then(scaffoldModifier)
            .then(modifier),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            if (topBar != null) {
                topBar(scrollBehavior!!)
            } else if (title?.isNotEmpty() == true) {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    navigationIcon = { navIcon?.invoke() },
                    title = { Text(title) },
                    actions = { actions(this) }
                )
            }
        },
        content = { content(it) }
    )
}
