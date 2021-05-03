package com.example.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetTask(tasks: Tasks)

    @Update
    suspend fun updateTask(tasks: Tasks)

    fun displayAllTask(
        SearchName: String,
        hideComplete: Boolean,
        SortBy: SortOrder
    ):Flow<List<Tasks>> {
       return when(SortBy)
       {
           SortOrder.BY_DATE ->{
               getSortByName(SearchName,hideComplete)
           }
           SortOrder.BY_NAME ->{
               getSortByDate(SearchName,hideComplete)
           }
       }
    }

    @Query("select * From Task_Table where (complete !=:hideComplete Or complete = 0) and name like '%' || :SearchName  || '%' order by important desc,name")
    fun getSortByName(SearchName: String, hideComplete: Boolean): Flow<List<Tasks>>

    @Query("select * From Task_Table where (complete !=:hideComplete Or complete = 0) and name like '%' || :SearchName  || '%' order by important desc,created")
    fun getSortByDate(SearchName: String, hideComplete: Boolean): Flow<List<Tasks>>

    @Delete
    suspend fun deleteTask(tasks: Tasks)

    @Query("delete  From Task_Table where complete=1")
    suspend fun deleteAllTask()
}