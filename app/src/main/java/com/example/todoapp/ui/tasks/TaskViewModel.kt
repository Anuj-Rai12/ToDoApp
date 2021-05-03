package com.example.todoapp.ui.tasks

import androidx.lifecycle.*
import com.example.todoapp.data.ClassPersistence
import com.example.todoapp.data.SortOrder
import com.example.todoapp.data.Tasks
import com.example.todoapp.data.TasksDao
import com.example.todoapp.utils.Event
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

    private val getLiveData = MutableLiveData<Tasks>()
    val getLiveDataNow: LiveData<Tasks>
        get() = getLiveData

    var getRes: Boolean? = null

    private val getMsgLiveData = MutableLiveData<Event<String>>()
    val getMsgLiveDataNow: LiveData<Event<String>>
        get() = getMsgLiveData

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

    fun itemClicked(tasks: Tasks, option: Boolean = true) {
        getLiveData.value = tasks
        getRes = option
    }

    fun updateCheckBox(tasks: Tasks, click: Boolean) = viewModelScope.launch {
        tasksDao.updateTask(tasks.copy(complete = click))
    }

    fun initial() {
        getRes = null
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

    fun onAddOrUpdateItem(name: String?, imp: Boolean, res: Int?) {
        if (name.isNullOrBlank()) {
            getMsgLiveData.value = Event("Enter Some Value")
        } else {
            insertOrUpdateData(name, imp, res)
        }
    }

    private fun insertOrUpdateData(name: String, imp: Boolean, res: Int?) = viewModelScope.launch {
        if (res == null) {
            tasksDao.insetTask(Tasks(0, name, important = imp))
            getMsgLiveData.value = Event("New Task Added Successfully")
        } else {
            tasksDao.updateTask(Tasks(res, name = name, important = imp))
            getMsgLiveData.value = Event("Task Is Updated Successfully")
        }
    }

    fun deleteAll() = viewModelScope.launch {
        tasksDao.deleteAllTask()
    }

    @ExperimentalCoroutinesApi
    val getData = getDataS

    sealed class TaskEvent {
        data class ShowTasksMessage(val tasks: Tasks) : TaskEvent()
    }

}
