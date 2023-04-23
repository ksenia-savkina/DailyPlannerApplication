package com.example.dailyplannerapplication.core

import androidx.room.TypeConverter
import java.sql.Timestamp

class Converters {

    @TypeConverter
    fun fromTimestamp(value: String?): Timestamp? {
        return value?.let { Timestamp.valueOf(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Timestamp?): String? {
        return date?.toString()
    }
}