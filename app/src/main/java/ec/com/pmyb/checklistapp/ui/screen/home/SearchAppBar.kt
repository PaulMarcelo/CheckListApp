package ec.com.pmyb.checklistapp.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ec.com.pmyb.checklistapp.R
import ec.com.pmyb.checklistapp.ui.screen.UtilColorApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var searchQuery by rememberSaveable { mutableStateOf(context.getString(R.string.empty)) }
    val activeChanged: (Boolean) -> Unit = { _ ->
        searchQuery = context.getString(R.string.empty)
        onQueryChange(context.getString(R.string.empty))
    }

    val focusRequester = remember { FocusRequester() }
    val windowInfo = LocalWindowInfo.current

    val isSystemInDarkTheme = isSystemInDarkTheme()
    val textColor = UtilColorApp.getTextColor(isSystemInDarkTheme)
    val iconColor = UtilColorApp.getIconColor(isSystemInDarkTheme)
    val backgroundColor = UtilColorApp.backgroundSearchBarColor(isSystemInDarkTheme)

    SearchBar(
        query = searchQuery,
        onQueryChange = { query ->
            searchQuery = query
            onQueryChange(query)
        },
        onSearch = onQueryChange,
        active = false,
        onActiveChange = activeChanged,
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        placeholder = { Text(stringResource(R.string.find), color = textColor) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Rounded.Clear,
                contentDescription = null,
                tint = iconColor,
                modifier = modifier.clickable {
                    searchQuery = context.getString(R.string.empty)
                    onQueryChange(context.getString(R.string.empty))
                }
            )
        },
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(
            top = 0.dp,
            bottom = 0.dp
        ),
        colors = SearchBarDefaults.colors(inputFieldColors = TextFieldDefaults.colors(textColor),
            containerColor = backgroundColor)
    ) {

    }

    LaunchedEffect(windowInfo) {
        snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
            if (isWindowFocused) {
                focusRequester.requestFocus()
            }
        }
    }

}