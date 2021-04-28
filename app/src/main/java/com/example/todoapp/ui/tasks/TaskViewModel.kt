package com.example.todoapp.ui.tasks

import androidx.lifecycle.*
import com.example.todoapp.data.Tasks
import com.example.todoapp.data.TasksDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val tasksDao: TasksDao) : ViewModel() {

    private val datalist = MutableLiveData<List<Tasks>>()
    val dataList_: LiveData<List<Tasks>>
        get() = datalist

     fun dataEntry(): Job = viewModelScope.launch {
         datalist.value=tasksDao.displayAllTask()
    }
}