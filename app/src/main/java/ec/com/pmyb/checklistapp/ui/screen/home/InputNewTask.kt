package ec.com.pmyb.checklistapp.ui.screen.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import ec.com.pmyb.checklistapp.R
import ec.com.pmyb.checklistapp.ui.screen.UtilColorApp
import ec.com.pmyb.checklistapp.ui.viewmodel.TasksViewModel

@Composable
fun InputNewTask(
    context: Context,
    taskViewModel: TasksViewModel,
    scrollUpStateAction: (Boolean) -> Unit
) {
    var task by remember {
        mutableStateOf("")
    }
    val isSystemInDarkTheme = isSystemInDarkTheme()
    val textColor = UtilColorApp.getTextColor(isSystemInDarkTheme)
    val iconColor = UtilColorApp.getIconColor(isSystemInDarkTheme)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.weight(1f),
            label = { Text(text = stringResource(R.string.new_task), color = textColor) },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            value = task,
            onValueChange = { it: String ->
                task = it
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    saveTask(
                        context, task.trim(), taskViewModel,
                        clearText = {
                            task = context.getString(R.string.empty)
                            scrollUpStateAction(true)
                        })
                }),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = context.getString(R.string.empty),
                    tint = iconColor,
                    modifier = Modifier.clickable(onClick = {
                        saveTask(
                            context, task.trim(), taskViewModel,
                            clearText = {
                                task = context.getString(R.string.empty)
                                scrollUpStateAction(true)
                            })
                    })
                )
            })
    }
}


private fun saveTask(
    context: Context, task: String, taskViewModel: TasksViewModel,
    clearText: () -> Unit
) {
    if (task.isEmpty()) {
        Toast.makeText(context, context.getString(R.string.required_text), Toast.LENGTH_SHORT)
            .show()
    } else {
        taskViewModel.createTask(task)
        clearText()
    }
}