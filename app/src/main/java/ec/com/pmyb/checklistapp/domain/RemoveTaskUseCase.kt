package ec.com.pmyb.checklistapp.domain

import ec.com.pmyb.checklistapp.data.repository.TaskRepository
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import javax.inject.Inject

class RemoveTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel){
        repository.remove(taskModel)
    }
}
class RemoveAllTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(){
        repository.removeAll()
    }
}
class RemoveSelectedTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(){
        repository.removeSelected()
    }
}