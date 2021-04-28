package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetTask(tasks: Tasks)

    @Update
    suspend fun updateTask(tasks: Tasks)

    @Query("select * From Task_Table")
    suspend fun displayAllTask(): List<Tasks>

    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Query("delete  From Task_Table")
    suspend fun deleteAllTask()

}