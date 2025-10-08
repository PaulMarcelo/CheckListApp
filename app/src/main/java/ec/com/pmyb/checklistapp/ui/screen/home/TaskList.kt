package ec.com.pmyb.checklistapp.ui.screen.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import ec.com.pmyb.checklistapp.ui.screen.UtilColorApp
import ec.com.pmyb.checklistapp.ui.theme.AppColors
import ec.com.pmyb.checklistapp.ui.viewmodel.TasksViewModel
import ec.com.pmyb.checklistapp.util.SwipeToDeleteContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskList(
    tasks: List<TaskModel>,
    taskViewModel: TasksViewModel,
    textFilter: String,
    scrollUpState: Boolean,
    resetSwipeState: Boolean,
    onResetSwipeConsumed: () -> Unit,
    totalItems: (Int) -> Unit,
    totalItemsSelected: (Int) -> Unit,
    totalItemsUnselected: (Int) -> Unit,
    scrollUpStateAction: (Boolean) -> Unit
) {
    val stateScrollLazyColumn = rememberLazyListState()
    LazyColumn(
        modifier =
        Modifier
            .padding(bottom = 30.dp)
            .imePadding(),
        state = stateScrollLazyColumn
    ) {
        val filteredTask = taskViewModel.filter(textFilter, tasks)
        items(filteredTask, key = { it.id }) { it ->
            Box(
                modifier = Modifier.animateItemPlacement(),
            ) {
                SwipeToDeleteContainer(
                    item = it, 
                    onDeleteRequest = {
                        taskViewModel.onSwipeDeleteDialogShow(it)
                    },
                    onResetSwipe = {
                        if (resetSwipeState) {
                            onResetSwipeConsumed()
                        }
                    }
                ) {
                    ItemTask(
                        taskModel = it,
                        taskViewModel = taskViewModel
                    ) {
                        scrollUpStateAction(it)
                    }
                }
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            val total = filteredTask.size
            val selected = taskViewModel.countSelected(filteredTask)
            val unselected = total - selected
            totalItems(total)
            totalItemsSelected(selected)
            totalItemsUnselected(unselected)
            if (scrollUpState) {
                stateScrollLazyColumn.scrollToItem(0, 0)
                delay(2000)
                taskViewModel.resetAllItemAnimate(filteredTask)
            }
        }
    }


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemTask(
    taskModel: TaskModel, taskViewModel: TasksViewModel,
    scrollUpStateAction: (Boolean) -> Unit
) {
    val updatedTaskModel = rememberUpdatedState(taskModel)
    var animateColor by remember { mutableStateOf(false) }
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val backgrounColorCard = UtilColorApp.backgroundCardItem(isSystemInDarkTheme)
    val colorText = UtilColorApp.getTextColor(isSystemInDarkTheme)
    val colorTextDate = UtilColorApp.getTextDateColor(isSystemInDarkTheme)
    val colorTextThrough = UtilColorApp.getSubTextColor(isSystemInDarkTheme)

    LaunchedEffect(updatedTaskModel) {
        if (updatedTaskModel.value.showAnimate) {
            animateColor = true
            delay(200)
            animateColor = false
            taskViewModel.resetTextToShowAnimate()
        }
    }
    Card(
        modifier = Modifier
            .combinedClickable { }
            .padding(2.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    taskViewModel.setTaskToUpdate(updatedTaskModel.value)
                    taskViewModel.onDlgShow()
                })
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgrounColorCard)
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .background(
                    if (animateColor) {
                        AppColors.Red2
                    } else {
                        Color.Transparent
                    }
                )
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = taskModel.createDate.dropLast(3),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    style = TextStyle(
                        fontSize = TextUnit(value = 10f, type = TextUnitType.Sp)
                    ),
                    color = colorTextDate
                )
                Checkbox(
                    checked = taskModel.selected,
                    onCheckedChange = {
                        taskViewModel.onCheckBoxSeleted(taskModel)
                        scrollUpStateAction(false)
                    },
                    colors = CheckboxDefaults.colors(checkmarkColor = Color.White),
                    modifier = Modifier
                        .size(DpSize(height = 30.dp, width = 30.dp))
                        .padding(end = 10.dp)
                )
            }
            HorizontalDivider()
            Row(
                Modifier.fillMaxWidth()
            ) {
                val styleTextWhenIsChecked: TextDecoration
                val styleColorText: Color
                if (taskModel.selected) {
                    styleTextWhenIsChecked = TextDecoration.LineThrough
                    styleColorText = colorTextThrough
                } else {
                    styleTextWhenIsChecked = TextDecoration.None
                    styleColorText = colorText
                }
                Text(
                    text = taskModel.task, modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp, vertical = 3.dp),
                    style = TextStyle(
                        textDecoration = styleTextWhenIsChecked
                    ),
                    color = styleColorText
                )
            }
        }
    }

}