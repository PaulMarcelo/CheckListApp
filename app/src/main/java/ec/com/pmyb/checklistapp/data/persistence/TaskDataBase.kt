package ec.com.pmyb.checklistapp.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import ec.com.pmyb.checklistapp.data.persistence.dao.TaskDao
import ec.com.pmyb.checklistapp.data.persistence.model.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDataBase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}