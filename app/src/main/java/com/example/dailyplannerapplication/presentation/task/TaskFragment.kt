package com.example.dailyplannerapplication.presentation.task

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dailyplannerapplication.R
import com.example.dailyplannerapplication.core.APP
import com.example.dailyplannerapplication.core.TAG_TASK
import com.example.dailyplannerapplication.databinding.FragmentTaskBinding
import com.example.dailyplannerapplication.domain.model.Task
import java.sql.Timestamp
import java.util.*

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var currentTask: Task

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTaskBinding.inflate(layoutInflater, container, false)
        currentTask = arguments?.getSerializable(TAG_TASK, Task::class.java)!!
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        val dateStart = currentTask.dateStart!!.toCalendar()
        val dateFinish = currentTask.dateFinish!!.toCalendar()

        binding.textViewName.text = currentTask.name
        binding.textViewDescription.text = currentTask.description
        binding.textViewDate.setInitialDate(dateStart)
        binding.textViewTimeStart.setInitialTime(dateStart)
        binding.textViewTimeFinish.setInitialTime(dateFinish)

        binding.buttonDelete.setOnClickListener {
            viewModel.deleteTask(currentTask) {}
            APP.navController.navigate(R.id.action_taskFragment_to_tasksFragment)
        }

        binding.buttonBack.setOnClickListener {
            APP.navController.navigate(R.id.action_taskFragment_to_tasksFragment)
        }
    }

    private fun Timestamp.toCalendar(): Calendar {
        val calendarDate = Calendar.getInstance()
        calendarDate.time = this
        return calendarDate
    }

    private fun TextView.setInitialDate(dateAndTime: Calendar) {
        this.text = DateUtils.formatDateTime(
            context,
            dateAndTime.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        )
    }

    private fun TextView.setInitialTime(dateAndTime: Calendar) {
        this.text = DateUtils.formatDateTime(
            context,
            dateAndTime.timeInMillis,
            DateUtils.FORMAT_SHOW_TIME
        )
    }
}