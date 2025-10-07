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
    @Query("SELECT * FROM TaskEntity ORDER BY createDate DESC")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM TaskEntity WHERE task LIKE '%' || :text || '%' ORDER BY createDate DESC")
    fun searchByText(text:String): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(item: TaskEntity)

    @Update
    suspend fun update(item: TaskEntity)

    @Delete
    suspend fun remove(item: TaskEntity)

    @Query("DELETE FROM TaskEntity ")
    suspend fun removeAll()

    @Query("DELETE FROM TaskEntity WHERE selected=1 ")
    suspend fun removeSelected()


}