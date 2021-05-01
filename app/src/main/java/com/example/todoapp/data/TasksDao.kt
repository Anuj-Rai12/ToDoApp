package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetTask(tasks: Tasks)

    @Update
    suspend fun updateTask(tasks: Tasks)

    @Query("select * From Task_Table where name like '%' || :name  || '%' order by important desc")
     fun displayAllTask(name:String): Flow<List<Tasks>>

    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Query("delete  From Task_Table")
    suspend fun deleteAllTask()
}