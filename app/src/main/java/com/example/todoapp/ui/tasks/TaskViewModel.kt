package com.example.todoapp.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.todoapp.data.TasksDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val tasksDao: TasksDao) : ViewModel() {

    val getStateData= MutableStateFlow("")
    private val getData_=getStateData.flatMapLatest {
        tasksDao.displayAllTask(it)
    }

val getData=getData_.asLiveData()

}