package com.example.dailyplannerapplication.presentation.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.dailyplannerapplication.core.REPOSITORY
import com.example.dailyplannerapplication.data.datasource.db.TaskDBDataSource
import com.example.dailyplannerapplication.data.repository.TaskRepositoryImpl
import com.example.dailyplannerapplication.domain.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    fun initDatabase() {
        val daoTask = TaskDBDataSource.getInstance(context).getTaskDao()
        REPOSITORY = TaskRepositoryImpl(daoTask)
    }

    fun getAllTasks(): LiveData<List<Task>> {
        return REPOSITORY.getTasks
    }

    fun getTasksByDate(dateStart: Timestamp): LiveData<List<Task>> {
        return REPOSITORY.getTasksByDate(dateStart)
    }

    fun createTask(task: Task, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.createTask(task) {
                onSuccess()
            }
        }

    fun deleteTask(task: Task, onSuccess: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            REPOSITORY.deleteTask(task) {
                onSuccess()
            }
        }
}