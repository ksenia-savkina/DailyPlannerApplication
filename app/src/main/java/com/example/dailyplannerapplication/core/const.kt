package com.example.dailyplannerapplication.core

import com.example.dailyplannerapplication.domain.repository.TaskRepository
import com.example.dailyplannerapplication.presentation.MainActivity

lateinit var APP: MainActivity
lateinit var REPOSITORY: TaskRepository
const val TAG_SELECTED_DATE = "selectedDate"
const val TAG_TASK = "task"