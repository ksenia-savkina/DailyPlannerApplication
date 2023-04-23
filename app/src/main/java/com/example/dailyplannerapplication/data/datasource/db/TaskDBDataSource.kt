package com.example.dailyplannerapplication.data.datasource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailyplannerapplication.core.Converters
import com.example.dailyplannerapplication.data.dao.TaskDao
import com.example.dailyplannerapplication.data.datasource.db.entity.TaskDBEntity

@Database(entities = [TaskDBEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaskDBDataSource : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao

    companion object {
        private var database: TaskDBDataSource? = null

        @Synchronized
        fun getInstance(context: Context): TaskDBDataSource {
            return if (database == null) {
                database =
                    Room.databaseBuilder(context, TaskDBDataSource::class.java, "plannerDB").build()
                database as TaskDBDataSource
            } else {
                database as TaskDBDataSource
            }
        }
    }
}