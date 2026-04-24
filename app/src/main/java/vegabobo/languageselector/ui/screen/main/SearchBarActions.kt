package vegabobo.languageselector.ui.screen.main

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vegabobo.languageselector.R
import vegabobo.languageselector.ui.components.FilterLabel
import vegabobo.languageselector.ui.theme.LanguageSelector

@Composable
fun SearchBarActions(
    modifier: Modifier = Modifier,
    isShowingUserApps: Boolean,
    isShowingSystemApps: Boolean,
    onClickToggleUserApps: () -> Unit,
    onClickToggleSystemApps: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterLabel(
            title = stringResource(R.string.show_user_apps),
            onClick = { onClickToggleUserApps() },
            isSelected = isShowingUserApps
        )
        FilterLabel(
            title = stringResource(R.string.show_system_apps),
            onClick = { onClickToggleSystemApps() },
            isSelected = isShowingSystemApps
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchBarActionsPreview() {
    LanguageSelector(dynamicColor = false) {
        SearchBarActions(
            isShowingUserApps = true,
            isShowingSystemApps = false,
            onClickToggleUserApps = {},
            onClickToggleSystemApps = {}
        )
    }
}
