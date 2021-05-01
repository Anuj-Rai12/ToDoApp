package com.example.todoapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val PREFERENCES_USER = "User_Preference"
private const val TAG = "Error In Reading Data"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_USER)

@Singleton
class ClassPersistence @Inject constructor(@ApplicationContext val context: Context) {
//Read Data

    val readDataStore = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.i(TAG, ":${exception.message}")
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val sortOrder = SortOrder.valueOf(
            preferences[MyPreferences.SORT_ORDER] ?: SortOrder.BY_DATE.name
        )
        val HIDE_COMPLETED = preferences[MyPreferences.HIDE_COMPLETE] ?: false
        filterCompleted(sortOrder, HIDE_COMPLETED)
    }

    suspend fun updateSortData(sortOrder: SortOrder) {
        context.dataStore.edit {
            it[MyPreferences.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHIDEData(hide: Boolean) {
        context.dataStore.edit {
            it[MyPreferences.HIDE_COMPLETE] = hide
        }
    }

    private object MyPreferences {
        val SORT_ORDER = stringPreferencesKey("SORT_ORDER")
        val HIDE_COMPLETE = booleanPreferencesKey("hide_completed")
    }
}

data class filterCompleted(val sortOrder: SortOrder, val HIDE_COMPLETED: Boolean)
enum class SortOrder {
    BY_NAME,
    BY_DATE
}