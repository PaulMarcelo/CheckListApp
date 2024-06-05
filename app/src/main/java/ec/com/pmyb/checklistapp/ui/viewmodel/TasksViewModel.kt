package ec.com.pmyb.checklistapp.ui.viewmodel


import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ec.com.pmyb.checklistapp.domain.AddTaskUseCase
import ec.com.pmyb.checklistapp.domain.GetTaskUseCase
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import ec.com.pmyb.checklistapp.ui.state.TaskUIState
import ec.com.pmyb.checklistapp.ui.state.TaskUIState.Success
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    getTaskUseCase: GetTaskUseCase,
    private var addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    val uiState: StateFlow<TaskUIState> = getTaskUseCase().map(::Success)
        .catch { TaskUIState.Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TaskUIState.Loading)


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
        val task = TaskModel(task = it)
        this._tasks.add(task)
        viewModelScope.launch {
            addTaskUseCase(task)
        }
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