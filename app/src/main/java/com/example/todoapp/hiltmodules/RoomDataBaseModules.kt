package com.example.todoapp.hiltmodules

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.RoomDataBaseInstance
import com.example.todoapp.data.TasksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDataBaseModules {

    @Singleton
    @Provides
    fun provideDataBase(
        app: Application, callback: RoomDataBaseInstance.Callback
    ): RoomDataBaseInstance {
        return Room.databaseBuilder(
            app,
            RoomDataBaseInstance::class.java,
            RoomDataBaseInstance.databaseName
        ).fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun provideDao(roomDataBaseInstance: RoomDataBaseInstance): TasksDao {
        return roomDataBaseInstance.taskDao()
    }

    @ApplicationScope
    @Singleton
    @Provides
    fun providesCoroutines() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope