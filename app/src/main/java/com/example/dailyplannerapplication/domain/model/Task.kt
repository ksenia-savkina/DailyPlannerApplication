package com.example.dailyplannerapplication.domain.model

import com.example.dailyplannerapplication.data.datasource.db.entity.TaskDBEntity
import java.io.Serializable
import java.sql.Timestamp


data class Task(
    val id: Int,
    val dateStart: Timestamp?,
    val dateFinish: Timestamp?,
    val name: String?,
    val description: String?
) : Serializable {
    fun mapToDBModel() = TaskDBEntity(
        id, dateStart!!, dateFinish!!, name!!, description!!
    )
}