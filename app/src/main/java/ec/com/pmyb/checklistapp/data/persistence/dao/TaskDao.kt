package ec.com.pmyb.checklistapp.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ec.com.pmyb.checklistapp.data.persistence.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskEntity")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(item:TaskEntity)
}