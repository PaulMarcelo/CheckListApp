package ec.com.pmyb.checklistapp.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import ec.com.pmyb.checklistapp.R
import ec.com.pmyb.checklistapp.ui.screen.UtilColorApp
import ec.com.pmyb.checklistapp.ui.state.TaskUIState
import ec.com.pmyb.checklistapp.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(navController: NavHostController, taskViewModel: TasksViewModel) {
    val lifeCycle = LocalLifecycleOwner.current.lifecycle
    val uiState by produceState<TaskUIState>(
        initialValue = TaskUIState.Loading,
        key1 = lifeCycle,
        key2 = taskViewModel
    ) {
        lifeCycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            taskViewModel.uiState.collect { value = it }
        }
    }
    val context = LocalContext.current
    when (uiState) {
        is TaskUIState.Error -> {
            val error = (uiState as TaskUIState.Error)
            Toast.makeText(context,"Error: ${error.throwable}", Toast.LENGTH_SHORT)
                .show()
        }
        is TaskUIState.Loading -> {
            LoadingPage()
        }

        is TaskUIState.Success -> {
            Body(taskViewModel, uiState)
        }
    }
}

@Composable
fun Body(taskViewModel: TasksViewModel, uiState: TaskUIState) {
    val listTask = (uiState as TaskUIState.Success).tasks
    val context = LocalContext.current
    var showSearchState by remember { mutableStateOf(false) }
    var textFilterState by remember { mutableStateOf(context.getString(R.string.empty)) }
    var totalState by remember { mutableIntStateOf(listTask.size) }
    var totalUnselectedState by remember { mutableIntStateOf(0) }
    var totalSelectedState by remember { mutableIntStateOf(0) }
    var scrollUpState by remember { mutableStateOf(false) }
    val showDialog: Boolean by taskViewModel.showDlg.observeAsState(initial = false)
    val showDeleteAllDialog: Boolean by taskViewModel.showDeleteAllDialog.observeAsState(initial = false)
    val showDeleteSelectedDialog: Boolean by taskViewModel.showDeleteSelectedDialog.observeAsState(initial = false)
    val showSwipeDeleteDialog: Boolean by taskViewModel.showSwipeDeleteDialog.observeAsState(initial = false)
    val resetSwipeState: Boolean by taskViewModel.resetSwipeState.observeAsState(initial = false)

    Scaffold(
        topBar = {
            AppBar(
                totalAdded = totalState,
                totalUnselected = totalUnselectedState,
                totalSelected = totalSelectedState,
                showSearch = {
                    showSearchState = !showSearchState
                    if (!showSearchState) {
                        textFilterState = context.getString(R.string.empty)
                    }
                    return@AppBar showSearchState
                },
                showDeleteAllDialog = {
                    taskViewModel.onDeleteAllDialogShow()
                },
                onDeleteSelectedRequest = {
                    if (taskViewModel.hasSelectedTasks(listTask)) {
                        taskViewModel.onDeleteSelectedDialogShow(listTask)
                    } else {
                        Toast.makeText(context, context.getString(R.string.no_items_selected), Toast.LENGTH_SHORT).show()
                    }
                })
        }
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (showSearchState) {
                    SearchAppBar(
                        onQueryChange = {
                            textFilterState = it
                        },
                    )
                } else {
                    InputNewTask(context, taskViewModel) {
                        scrollUpState = it
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                TaskList(
                    listTask, taskViewModel, textFilterState,
                    scrollUpState = scrollUpState,
                    resetSwipeState = resetSwipeState,
                    onResetSwipeConsumed = {
                        taskViewModel.resetSwipeStateConsumed()
                    },
                    totalItems = {
                        totalState = it
                    },
                    totalItemsSelected = {
                        totalSelectedState = it
                    },
                    totalItemsUnselected = {
                        totalUnselectedState = it
                    },
                    scrollUpStateAction = {
                        scrollUpState = it
                    }
                )
                UpdateTaskDialog(
                    show = showDialog,
                    taskViewModel = taskViewModel,
                    onDissmiss = { taskViewModel.onDlgClose() },
                    onTaskUpdate = {
                        taskViewModel.onTasksUpdate(it)
                    }
                )
                
                // Diálogos de confirmación para eliminación
                DeleteAllConfirmationDialog(
                    show = showDeleteAllDialog,
                    onDismiss = { taskViewModel.onDeleteAllDialogClose() },
                    onConfirm = { taskViewModel.removeAll() }
                )
                
                DeleteSelectedConfirmationDialog(
                    show = showDeleteSelectedDialog,
                    onDismiss = { taskViewModel.onDeleteSelectedDialogClose() },
                    onConfirm = { taskViewModel.removeSelected() }
                )
                
                DeleteTaskConfirmationDialog(
                    show = showSwipeDeleteDialog,
                    onDismiss = { taskViewModel.onSwipeDeleteDialogClose() },
                    onConfirm = { taskViewModel.onSwipeDeleteConfirmed() }
                )
            }
        }
    }
}

@Composable
fun UpdateTaskDialog(
    show: Boolean,
    taskViewModel: TasksViewModel,
    onDissmiss: () -> Unit,
    onTaskUpdate: (String) -> Unit
) {
    if (show) {
        val context = LocalContext.current
        val taskSelected = taskViewModel.taskToUpdate
        var newTask by remember {
            mutableStateOf(taskSelected.value?.task ?: context.getString(R.string.empty))
        }
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current

        val isSystemInDarkTheme = isSystemInDarkTheme()
        val principalColorApp = UtilColorApp.getModeAppPrincipalColor(isSystemInDarkTheme)
        val textColor = UtilColorApp.getTextColor(isSystemInDarkTheme)
        val bagroundColor = UtilColorApp.backgroundDialogColor(isSystemInDarkTheme)
        CustomDialog(
            title = {
                Surface(
                    color = principalColorApp,
                    contentColor = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.my_list),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 16.dp),
                        color = textColor,
                    )
                }
            },
            text = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(color = bagroundColor)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 8.dp, vertical = 16.dp)
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    keyboardController?.show()
                                }
                            },
                        value = newTask,
                        singleLine = true, maxLines = 1,
                        onValueChange = {
                            newTask = it
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.update_task),
                                color = textColor
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = textColor,
                            unfocusedBorderColor = textColor,
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            onTaskUpdate(newTask.trim())
                            newTask = context.getString(R.string.empty)
                            taskViewModel.onDlgClose()
                        })
                    )
                }
            },
            onDismissRequest = { onDissmiss() },
            confirmButton = {
                TextButton(onClick = {
                    onTaskUpdate(newTask)
                    newTask = context.getString(R.string.empty)
                    taskViewModel.onDlgClose()
                }) {
                    Text(
                        text = stringResource(R.string.accept),
                        color = textColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {}
        )
    }
}