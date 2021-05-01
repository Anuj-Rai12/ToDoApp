package com.example.todoapp.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.ClassPersistence
import com.example.todoapp.data.SortOrder
import com.example.todoapp.data.TasksDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val tasksDao: TasksDao, private val classPersistence: ClassPersistence
) : ViewModel() {

    val getStateData = MutableStateFlow("")

    val preferencesFlow = classPersistence.readDataStore

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

    @ExperimentalCoroutinesApi
    val getData = getDataS

}
