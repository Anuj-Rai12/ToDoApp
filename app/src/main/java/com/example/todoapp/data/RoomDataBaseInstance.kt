package com.example.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp.hiltmodules.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Tasks::class], version = 1,exportSchema = false)
abstract class RoomDataBaseInstance : RoomDatabase() {
    abstract fun taskDao(): TasksDao

    companion object {
        const val databaseName = "TaskTable"
    }

    class Callback @Inject constructor(
        private val database: Provider<RoomDataBaseInstance>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao=database.get().taskDao()
            applicationScope.launch {
                dao.insetTask(Tasks(0,"ash the dishes"))
                dao.insetTask(Tasks(0,"Do the laundry"))
                dao.insetTask(Tasks(0,"Buy groceries", important = true))
                dao.insetTask(Tasks(0,"Prepare food", complete = true))
                dao.insetTask(Tasks(0,"Call mom"))
                dao.insetTask(Tasks(0,"Visit grandma", complete = true))
                dao.insetTask(Tasks(0,"Repair my bike"))
                dao.insetTask(Tasks(0,"Call Elon Musk"))
            }
        }
    }
}