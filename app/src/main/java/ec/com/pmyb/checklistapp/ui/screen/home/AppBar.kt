package ec.com.pmyb.checklistapp.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ec.com.pmyb.checklistapp.R
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import ec.com.pmyb.checklistapp.ui.screen.UtilApp
import ec.com.pmyb.checklistapp.ui.screen.UtilColorApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    totalAdded: Int,
    totalUnselected: Int,
    totalSelected: Int,
    showSearch: () -> Boolean,
    onDeleteAllRequest: () -> Unit,
    onDeleteSelectedRequest: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableIntStateOf(0) }
    var shouldShowDialog by remember { mutableStateOf(false) }
    var isShowSearch by remember { mutableStateOf(false) }
    val colorIcon = UtilColorApp.getIconColor(isSystemInDarkTheme())

    TopAppBar(
        title = {
            Column {
                Text(text = stringResource(R.string.my_list))
                Row {
                    var title1 = stringResource(R.string.added)
                    if (isShowSearch) {
                        title1 = stringResource(R.string.found)
                    }
                    Text(text = "$title1: $totalAdded", style = TextStyle(fontSize = 10.sp))
                    if (totalAdded > 0) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.pending, totalUnselected),
                            style = TextStyle(fontSize = 10.sp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.completed, totalSelected),
                            style = TextStyle(fontSize = 10.sp)
                        )
                    }
                }
            }
        },
        actions = {
            IconButton(
                onClick = {
                    isShowSearch = showSearch()
                }) {
                if (isShowSearch) {
                    Icon(
                        Icons.Filled.Close, null,
                        tint = colorIcon
                    )
                } else {
                    Icon(
                        Icons.Filled.Search, null,
                        tint = colorIcon
                    )
                }
            }
            Box(modifier = Modifier.padding(end = 8.dp)) {
                IconButton(
                    onClick = { expanded = true }) {
                    Icon(
                        Icons.Filled.MoreVert, null,
                        tint = colorIcon
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.delete_selected)) },
                        onClick = {
                            selectedOption = 1
                            expanded = false
                            onDeleteSelectedRequest()
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.delete_all)) },
                        onClick = {
                            selectedOption = 2
                            expanded = false
                            onDeleteAllRequest()
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.about)) },
                        onClick = {
                            selectedOption = 3
                            expanded = false
                            shouldShowDialog = true
                        })
                }
            }
        }
    )
    if (selectedOption == 3) {
        ShowDlgAbout(show = shouldShowDialog) {
            shouldShowDialog = false
        }
    }
}

@Composable
private fun ShowDlgAbout(
    show: Boolean, onDismiss: () -> Unit
) {
    if (show) {
        val isSystemInDarkTheme = isSystemInDarkTheme()
        val principalColorApp = UtilColorApp.getModeAppPrincipalColor(isSystemInDarkTheme)
        val textColor = UtilColorApp.getTextColor(isSystemInDarkTheme)
        val bagroundColor = UtilColorApp.backgroundDialogColor(isSystemInDarkTheme)
        val context = LocalContext.current
        CustomDialog(
            title = {
                Surface(
                    color = principalColorApp,
                    contentColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.information),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        color = textColor
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = bagroundColor)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "Bienvenido a nuestra aplicación\n\n" +
                                "\nEsta es una aplicación simple y diseñada para ayudar a recordar tareas pendientes. " +
                                "\nCon nuestra aplicación, puedes crear una lista de tareas y a medida que las completas las puedes ir marcando como realizadas." +
                                "\n\nNuestro deseo es proporcionar una herramienta útil y fácil de usar para las tareas del día a día." +
                                "\n\n¡Gracias por elegir nuestra aplicación!" +
                                "\n\nVersión ${UtilApp.getVersionName(context)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor
                    )
                }
            },
            onDismissRequest = { onDismiss() },
            confirmButton = {},
            dismissButton = {}
        )
    }
}
