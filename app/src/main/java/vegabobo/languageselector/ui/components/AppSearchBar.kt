package vegabobo.languageselector.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import vegabobo.languageselector.ui.theme.LanguageSelector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    query: String,
    onUpdatedValue: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .semantics { isTraversalGroup = true }
            .then(modifier),
        // 统一搜索框的圆角和左右留白，避免页面层重复配置。
        shape = RoundedCornerShape(24.dp),
        value = query,
        onValueChange = { onUpdatedValue(it) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun AppSearchBarPreview() {
    val query = remember { mutableStateOf("Github") }

    LanguageSelector(dynamicColor = false) {
        AppSearchBar(
            placeholder = "Search apps",
            query = query.value,
            onUpdatedValue = { query.value = it }
        )
    }
}
