package com.example.todoapp.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ClassPersistence
import com.example.todoapp.data.SortOrder
import com.example.todoapp.data.Tasks
import com.example.todoapp.data.TasksDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val tasksDao: TasksDao, private val classPersistence: ClassPersistence
) : ViewModel() {


    val getStateData = MutableStateFlow("")

    val preferencesFlow = classPersistence.readDataStore

    private val taskEventChannel = Channel<TaskEvent>()
    val taskEvent = taskEventChannel.receiveAsFlow()

    @ExperimentalCoroutinesApi
    private val getDataS = combine(
        preferencesFlow,
        getStateData
    ) { preferenceFlow, getStateDate ->
        Pair(preferenceFlow, getStateDate)
    }.flatMapLatest { (filter, getStateDate) ->
        tasksDao.displayAllTask(getStateDate, filter.HIDE_COMPLETED, filter.sortOrder)
    }.asLiveData()

    fun sortOrderData(sortOrder: SortOrder) = viewModelScope.launch {
        classPersistence.updateSortData(sortOrder)
    }

    fun hideOrderCompeted(hide: Boolean) = viewModelScope.launch {
        classPersistence.updateHIDEData(hide)
    }

    fun itemClicked(tasks: Tasks) {
        //Doing Same Time later
    }

    fun updateCheckBox(tasks: Tasks, click: Boolean) = viewModelScope.launch {
        tasksDao.updateTask(tasks.copy(complete = click))
    }

    fun deleteTaskData(currPos: Tasks?) = viewModelScope.launch {
        currPos?.let {
            tasksDao.deleteTask(it)
            taskEventChannel.send(TaskEvent.ShowTasksMessage(it))
        }
    }

    fun addNewTasks(tasks: Tasks) = viewModelScope.launch {
        tasksDao.insetTask(tasks)
    }
    @ExperimentalCoroutinesApi
    val getData = getDataS

    sealed class TaskEvent {
        data class ShowTasksMessage(val tasks: Tasks) : TaskEvent()
    }

}
