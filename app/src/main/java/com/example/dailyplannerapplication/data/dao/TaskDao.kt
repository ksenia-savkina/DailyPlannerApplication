package com.example.dailyplannerapplication.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dailyplannerapplication.data.datasource.db.entity.TaskDBEntity

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTask(model: TaskDBEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTask(model: TaskDBEntity)

    @Delete
    suspend fun deleteTask(model: TaskDBEntity)

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): LiveData<List<TaskDBEntity>>

    @Query("SELECT * FROM tasks WHERE dateStart LIKE :dateStart")
    fun getTasksByDate(dateStart: String): LiveData<List<TaskDBEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): LiveData<TaskDBEntity>
}