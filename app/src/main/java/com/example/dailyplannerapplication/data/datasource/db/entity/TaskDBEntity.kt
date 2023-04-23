package com.example.dailyplannerapplication.data.datasource.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dailyplannerapplication.domain.model.Task
import java.sql.Timestamp

@Entity(tableName = "tasks")
data class TaskDBEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateStart: Timestamp,
    val dateFinish: Timestamp,
    val name: String,
    val description: String
) {
    fun mapToModel() = Task(
        id, dateStart, dateFinish, name, description
    )
}