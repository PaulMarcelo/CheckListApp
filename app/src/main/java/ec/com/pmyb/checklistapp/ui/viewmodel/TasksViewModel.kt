package ec.com.pmyb.checklistapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ec.com.pmyb.checklistapp.domain.AddTaskUseCase
import ec.com.pmyb.checklistapp.domain.GetTaskUseCase
import ec.com.pmyb.checklistapp.domain.RemoveAllTaskUseCase
import ec.com.pmyb.checklistapp.domain.RemoveSelectedTaskUseCase
import ec.com.pmyb.checklistapp.domain.RemoveTaskUseCase
import ec.com.pmyb.checklistapp.domain.SearchTaskUseCase
import ec.com.pmyb.checklistapp.domain.UpdateTaskUseCase
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import ec.com.pmyb.checklistapp.ui.state.TaskUIState
import ec.com.pmyb.checklistapp.ui.state.TaskUIState.Success
import ec.com.pmyb.checklistapp.util.toSimpleString
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    getTaskUseCase: GetTaskUseCase,
    private var addTaskUseCase: AddTaskUseCase,
    private var updatTaskUseCase: UpdateTaskUseCase,
    private var removeTaskUseCase: RemoveTaskUseCase,
    private var removeAllTaskUseCase: RemoveAllTaskUseCase,
    private var removeSelectedTaskUseCase: RemoveSelectedTaskUseCase,
) : ViewModel() {

    val uiState: StateFlow<TaskUIState> = getTaskUseCase().map(::Success)
        .catch { TaskUIState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUIState.Loading)


    private val _showDlg = MutableLiveData<Boolean>()
    val showDlg: LiveData<Boolean> = _showDlg

    private val _taskToUpdate = MutableLiveData<TaskModel>()
    val taskToUpdate: LiveData<TaskModel> = _taskToUpdate

    private val _textToShowAnimate = MutableLiveData<String>()

    private val _showDeleteAllDialog = MutableLiveData<Boolean>()
    val showDeleteAllDialog: LiveData<Boolean> = _showDeleteAllDialog

    private val _showDeleteSelectedDialog = MutableLiveData<Boolean>()
    val showDeleteSelectedDialog: LiveData<Boolean> = _showDeleteSelectedDialog

    private val _showSwipeDeleteDialog = MutableLiveData<Boolean>()
    val showSwipeDeleteDialog: LiveData<Boolean> = _showSwipeDeleteDialog

    private val _taskToDelete = MutableLiveData<TaskModel>()
    val taskToDelete: LiveData<TaskModel> = _taskToDelete

    private val _resetSwipeState = MutableLiveData<Boolean>()
    val resetSwipeState: LiveData<Boolean> = _resetSwipeState

    fun onDlgClose() {
        _showDlg.value = false
    }

    fun onDlgShow() {
        _showDlg.value = true
    }

    fun onDeleteAllDialogShow(listTask: List<TaskModel>) {
        if (listTask.isNotEmpty()) {
            _showDeleteAllDialog.value = true
        } else {
            // No hay tareas en la lista, se mostrará un toast
            _showDeleteAllDialog.value = false
        }
    }

    fun hasTasks(listTask: List<TaskModel>): Boolean {
        return listTask.isNotEmpty()
    }

    fun onDeleteAllDialogClose() {
        _showDeleteAllDialog.value = false
    }

    fun onDeleteSelectedDialogShow(listTask: List<TaskModel>) {
        val selectedCount = countSelected(listTask)
        if (selectedCount > 0) {
            _showDeleteSelectedDialog.value = true
        } else {
            // No hay tareas seleccionadas, se mostrará un toast
            _showDeleteSelectedDialog.value = false
        }
    }

    fun hasSelectedTasks(listTask: List<TaskModel>): Boolean {
        return countSelected(listTask) > 0
    }

    fun onDeleteSelectedDialogClose() {
        _showDeleteSelectedDialog.value = false
    }

    fun onSwipeDeleteDialogShow(taskModel: TaskModel) {
        _taskToDelete.value = taskModel
        _showSwipeDeleteDialog.value = true
    }

    fun onSwipeDeleteDialogClose() {
        _showSwipeDeleteDialog.value = false
        _taskToDelete.value = null
        _resetSwipeState.value = true
    }

    fun onSwipeDeleteConfirmed() {
        _taskToDelete.value?.let { task ->
            viewModelScope.launch {
                removeTaskUseCase.invoke(task)
            }
        }
        _showSwipeDeleteDialog.value = false
        _taskToDelete.value = null
        _resetSwipeState.value = true
    }

    fun resetSwipeStateConsumed() {
        _resetSwipeState.value = false
    }

    fun setTaskToUpdate(taskModel: TaskModel) {
        this._taskToUpdate.value = taskModel
    }

    fun resetTextToShowAnimate() {
        this._textToShowAnimate.value = ""
    }

    fun createTask(it: String) {
        _textToShowAnimate.value = it
        val task = TaskModel(task = it, createDate = Date().toSimpleString())
        viewModelScope.launch {
            addTaskUseCase(task)
        }
        this.onDlgClose()
    }

    fun onCheckBoxSeleted(taskModel: TaskModel) {
        viewModelScope.launch {
            updatTaskUseCase.invoke(taskModel.copy(selected = !taskModel.selected))
        }
    }

    fun onTasksUpdate(newText: String) {
        _textToShowAnimate.value = newText
        viewModelScope.launch {
            updatTaskUseCase.invoke(
                taskToUpdate.value!!.copy(
                    task = newText
                )
            )
        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        viewModelScope.launch {
            removeTaskUseCase.invoke(taskModel)
        }
    }

    fun removeAll() {
        viewModelScope.launch {
            removeAllTaskUseCase.invoke()
        }
    }

    fun removeSelected() {
        viewModelScope.launch {
            removeSelectedTaskUseCase.invoke()
        }
    }

    fun filter(text: String, listTask: List<TaskModel>): List<TaskModel> {
        if (_textToShowAnimate.value?.isNotEmpty() == true) {
            listTask.forEach {
                it.showAnimate = it.task == _textToShowAnimate.value
            }
        } else {
            resetAllItemAnimate(listTask)
        }
        return if (text.isEmpty()) {
            listTask
        } else {
            listTask.filter { item ->
                item.task.lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            }.toMutableList()
        }
    }

    fun resetAllItemAnimate(listTask: List<TaskModel>) {
        val itemWithAnimateTrue = listTask.firstOrNull { it.showAnimate }
        itemWithAnimateTrue?.let {
            listTask.forEach {
                it.showAnimate = false
            }
        }
    }

    fun countSelected(listTask: List<TaskModel>): Int {
        return listTask.count { it.selected }
    }


}