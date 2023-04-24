package com.example.dailyplannerapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dailyplannerapplication.data.dao.TaskDao
import com.example.dailyplannerapplication.data.datasource.db.TaskDBDataSource
import com.example.dailyplannerapplication.domain.model.Task
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.sql.Timestamp

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TaskDBDataSource
    private lateinit var taskDao: TaskDao

    @Before
    fun setUp() {
        database = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDBDataSource::class.java,
            "plannerDB"
        ).build()
        taskDao = database.getTaskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert() = runBlocking {
        val task = Task(
            id = 0,
            dateStart = Timestamp.valueOf("2023-04-23 12:00:00"),
            dateFinish = Timestamp.valueOf("2023-04-23 14:00:00"),
            name = "testTask",
            description = "desc"
        )
        taskDao.createTask(task.mapToDBModel())

        val result = taskDao.getAllTasks().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("testTask", result[0].name)
    }

    @Test
    fun delete() = runBlocking {
        val newTask = Task(
            id = 0,
            dateStart = Timestamp.valueOf("2023-04-23 12:00:00"),
            dateFinish = Timestamp.valueOf("2023-04-23 14:00:00"),
            name = "deleteTask",
            description = "desc"
        )
        taskDao.createTask(newTask.mapToDBModel())
        val taskId = taskDao.getAllTasks().getOrAwaitValue().last().id

        val task = taskDao.getTaskById(taskId)
        taskDao.deleteTask(task.getOrAwaitValue())

        val result = taskDao.getAllTasks().getOrAwaitValue()

        Assert.assertEquals(0, result.size)
    }
}