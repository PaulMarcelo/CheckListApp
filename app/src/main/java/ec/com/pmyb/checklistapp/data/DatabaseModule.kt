package ec.com.pmyb.checklistapp.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ec.com.pmyb.checklistapp.data.persistence.TaskDataBase
import ec.com.pmyb.checklistapp.data.persistence.dao.TaskDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideTasksDao(taskDataBase: TaskDataBase):TaskDao{
        return taskDataBase.taskDao()
    }

    @Provides
    @Singleton
    fun provideTaskDataBase(@ApplicationContext appContext: Context): TaskDataBase {
        return Room.databaseBuilder(appContext, TaskDataBase::class.java, "taskDataBase").build()

    }
}