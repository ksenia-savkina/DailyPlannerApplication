package com.example.dailyplannerapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.dailyplannerapplication.data.dao.TaskDao
import com.example.dailyplannerapplication.data.datasource.db.entity.TaskDBEntity
import com.example.dailyplannerapplication.domain.model.Task
import com.example.dailyplannerapplication.domain.repository.TaskRepository
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override val getTasks: LiveData<List<Task>>
        get() = taskDao.getAllTasks().toMutableLiveDataList()

    override suspend fun createTask(model: Task, onSuccess: () -> Unit) {
        taskDao.createTask(model.mapToDBModel())
        onSuccess()
    }

    override suspend fun updateTask(model: Task, onSuccess: () -> Unit) {
        taskDao.updateTask(model.mapToDBModel())
        onSuccess()
    }

    override suspend fun deleteTask(model: Task, onSuccess: () -> Unit) {
        taskDao.deleteTask(model.mapToDBModel())
        onSuccess()
    }

    override suspend fun getTaskById(id: Int): LiveData<Task> {
        return taskDao.getTaskById(id).toMutableLiveData()
    }

    override fun getTasksByDate(dateStart: Timestamp): LiveData<List<Task>> {
        return taskDao.getTasksByDate(dateStart.getOnlyDate()).toMutableLiveDataList()
    }

    private fun Timestamp.getOnlyDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = sdf.format(this)
        return "$date%"
    }

    private fun LiveData<List<TaskDBEntity>>.toMutableLiveDataList(): MutableLiveData<List<Task>> {
        val mediatorLiveData = MediatorLiveData<List<Task>>()
        mediatorLiveData.addSource(this) {
            val list = ArrayList<Task>()
            for (t in it) {
                list.add(t.mapToModel())
            }
            mediatorLiveData.value = list
        }
        return mediatorLiveData
    }

    private fun LiveData<TaskDBEntity>.toMutableLiveData(): MutableLiveData<Task> {
        val mediatorLiveData = MediatorLiveData<Task>()
        mediatorLiveData.addSource(this) {
            mediatorLiveData.value = it.mapToModel()
        }
        return mediatorLiveData
    }
}