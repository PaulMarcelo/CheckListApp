package ec.com.pmyb.checklistapp.data.repository

import ec.com.pmyb.checklistapp.data.persistence.dao.TaskDao
import ec.com.pmyb.checklistapp.data.persistence.model.TaskEntity
import ec.com.pmyb.checklistapp.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    val tasks: Flow<List<TaskModel>> = taskDao.getTasks().map {
        it.map { TaskModel(it.id, it.task, it.selected) }
    }

    suspend fun add(taskmodel: TaskModel) {
        taskDao.addTask(taskmodel.ToEntity())
    }

    suspend fun update(taskmodel: TaskModel) {
        taskDao.update(taskmodel.ToEntity())
    }

    suspend fun remove(taskmodel: TaskModel) {
        taskDao.remove(taskmodel.ToEntity())
    }

}

fun TaskModel.ToEntity(): TaskEntity {
    return TaskEntity(
        this.id,
        this.task,
        this.selected
    )
}