package ec.com.pmyb.checklistapp.domain

import ec.com.pmyb.checklistapp.data.repository.TaskRepository
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class GetTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(): Flow<List<TaskModel>> = repository.tasks

}