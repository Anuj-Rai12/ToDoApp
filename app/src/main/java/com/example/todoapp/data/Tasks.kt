package com.example.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat


@Parcelize
@Entity(tableName = "Task_Table")
data class Tasks(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val name: String,
        val important: Boolean = false,
        val complete: Boolean = false,
        val created: Long = System.currentTimeMillis()
) : Parcelable {

    val createdCurrentTimeData: String
        get() = DateFormat.getDateTimeInstance().format(created)
}
