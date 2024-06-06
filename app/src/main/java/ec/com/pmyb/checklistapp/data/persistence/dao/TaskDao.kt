package ec.com.pmyb.checklistapp.data.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ec.com.pmyb.checklistapp.data.persistence.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM TaskEntity")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(item:TaskEntity)

    @Update
    suspend fun update(item: TaskEntity)
//    @Query("DELETE FROM yourDatabaseTable WHERE id = :id")
    @Delete
    suspend fun remove(item: TaskEntity)
}