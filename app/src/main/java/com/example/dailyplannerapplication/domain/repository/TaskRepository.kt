package com.example.dailyplannerapplication.domain.repository

import androidx.lifecycle.LiveData
import com.example.dailyplannerapplication.domain.model.Task
import java.sql.Timestamp

interface TaskRepository {

    suspend fun createTask(model: Task, onSuccess: () -> Unit)

    suspend fun updateTask(model: Task, onSuccess: () -> Unit)

    suspend fun deleteTask(model: Task, onSuccess: () -> Unit)

    val getTasks: LiveData<List<Task>>

    fun getTasksByDate(dateStart: Timestamp): LiveData<List<Task>>

    suspend fun getTaskById(id: Int): LiveData<Task>
}