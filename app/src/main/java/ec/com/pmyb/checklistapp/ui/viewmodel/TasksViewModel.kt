package ec.com.pmyb.checklistapp.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import javax.inject.Inject

class TasksViewModel @Inject constructor() : ViewModel() {

    private val _showDlg = MutableLiveData<Boolean>()
    val showDlg: LiveData<Boolean> = _showDlg

    private val _tasks = mutableStateListOf<TaskModel>()
    val taks: List<TaskModel> = _tasks

    fun onDlgClose() {
        _showDlg.value = false
    }

    fun onDlgShow() {
        _showDlg.value = true
    }

    fun createTask(it: String) {
        this._tasks.add(TaskModel(task = it))
        this.onDlgClose()
    }

    fun onCheckBoxSeleted(taskModel: TaskModel) {
        val index = _tasks.indexOf(taskModel)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        val taskFound = _tasks.find { it.id == taskModel.id }
        _tasks.remove(taskFound)
    }

}