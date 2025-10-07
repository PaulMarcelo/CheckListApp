package ec.com.pmyb.checklistapp.ui.state

import ec.com.pmyb.checklistapp.ui.model.TaskModel

sealed interface TaskUIState {

    data object Loading:TaskUIState

    data class Error(val throwable: Throwable):TaskUIState

    data class Success(val tasks:List<TaskModel>):TaskUIState


}