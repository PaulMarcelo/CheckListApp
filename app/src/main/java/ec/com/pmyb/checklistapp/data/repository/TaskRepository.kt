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

    var tasks: Flow<List<TaskModel>> = taskDao.getTasks().map {
        it.map { TaskModel(it.id, it.task, it.selected, it.createDate) }
    }

    fun searchByText(text: String) {
        tasks = taskDao.searchByText(text).map { it ->
            it.map { TaskModel(it.id, it.task, it.selected, it.createDate) }
        }
    }


    suspend fun add(taskmodel: TaskModel) {
        taskDao.addTask(taskmodel.toEntity())
    }

    suspend fun update(taskmodel: TaskModel) {
        taskDao.update(taskmodel.toEntity())
    }

    suspend fun remove(taskmodel: TaskModel) {
        taskDao.remove(taskmodel.toEntity())
    }

    suspend fun removeAll() {
        taskDao.removeAll()
    }

    suspend fun removeSelected() {
        taskDao.removeSelected()
    }

}

fun TaskModel.toEntity(): TaskEntity {
    return TaskEntity(
        this.id,
        this.task,
        this.selected,
        this.createDate
    )
}