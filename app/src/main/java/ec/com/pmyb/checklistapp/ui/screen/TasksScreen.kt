package ec.com.pmyb.checklistapp.ui.screen

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import ec.com.pmyb.checklistapp.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(taskViewModel: TasksViewModel) {

    val showDlg: Boolean by taskViewModel.showDlg.observeAsState(initial = false)

    Box(modifier = Modifier.fillMaxSize()) {
        showAddTaskDlg(show = showDlg, onDismiss = {
            taskViewModel.onDlgClose()
        }, onTaskAdded = {
            taskViewModel.createTask(it)
        })
        FabBtnAdd(Modifier.align(Alignment.BottomEnd), taskViewModel)
        TaskList(taskViewModel)
    }
}

@Composable
fun TaskList(taskViewModel: TasksViewModel) {
    val myTaks: List<TaskModel> = taskViewModel.taks
    LazyColumn {
        items(myTaks, key = { it.id }) { it ->
            ItemTask(taskModel = it, taskViewModel = taskViewModel)
        }
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, taskViewModel: TasksViewModel) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .pointerInput(Unit) {
                detectTapGestures (onLongPress = {
                    taskViewModel.onItemRemove(taskModel)
                })
            },
        shape = MaterialTheme.shapes.medium,
//        elevation = 5.dp,
//        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = taskModel.task, modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            )
            Checkbox(checked = taskModel.selected, onCheckedChange = {
                taskViewModel.onCheckBoxSeleted(taskModel)
            })
        }
    }
}

@Composable
private fun FabBtnAdd(modifier: Modifier, taskViewModel: TasksViewModel) {
    FloatingActionButton(
        onClick = {
            taskViewModel.onDlgShow()
        },
        modifier = modifier
            .padding(end = 30.dp, bottom = 60.dp)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@Composable
private fun showAddTaskDlg(
    show: Boolean, onDismiss: () -> Unit,
    onTaskAdded: (String) -> Unit
) {
    val context = LocalContext.current
    var task by remember {
        mutableStateOf("")
    }
    if (show) {
        Dialog(onDismissRequest = {
            //    onDismiss()
        }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Nueva Tarea", fontSize = 16.sp)
                            Icon(imageVector = Icons.Filled.Close, contentDescription = "",
                                tint = colorResource(id = android.R.color.darker_gray),
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(30.dp)
                                    .clickable {
                                        onDismiss()
                                    })
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(value = task,
//                            leadingIcon = {
//                                Icon(
//                                    Icons.Filled.Create, contentDescription = "",
//                                    modifier = Modifier
//                                        .width(20.dp)
//                                        .height(20.dp)
//                                )
//                            },
                            placeholder = { Text(text = "Tarea...") },
                            singleLine = true,
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = { it: String ->
                                task = it
                            }
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Button(onClick = {
                            onTaskAdded(task)
                            Toast.makeText(context, task, Toast.LENGTH_SHORT).show()
                        }, modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Aceptar")
                        }
                    }
                }
            }
        }
    }

}