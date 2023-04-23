package com.example.dailyplannerapplication.presentation.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplannerapplication.R
import com.example.dailyplannerapplication.core.APP
import com.example.dailyplannerapplication.core.TAG_SELECTED_DATE
import com.example.dailyplannerapplication.core.TAG_TASK
import com.example.dailyplannerapplication.databinding.FragmentTasksBinding
import com.example.dailyplannerapplication.domain.model.Task
import java.sql.Timestamp
import java.util.*

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var tableRecyclerView: RecyclerView
    private lateinit var tableRowAdapter: TableRowAdapter
    private lateinit var tasksList: List<Task>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTasksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        viewModel.initDatabase()
        tableRecyclerView = binding.tableRecyclerView
        tableRowAdapter = TableRowAdapter()
        tableRecyclerView.adapter = tableRowAdapter
        tasksList = ArrayList()

        var selectedDate = Timestamp(System.currentTimeMillis())
        loadData(selectedDate)

        binding.btnAdd.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(TAG_SELECTED_DATE, selectedDate.toString())
            APP.navController.navigate(R.id.action_tasksFragment_to_addTaskFragment, bundle)
        }

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendarDate = Calendar.getInstance()
            calendarDate.set(year, month, dayOfMonth)
            selectedDate = Timestamp(calendarDate.timeInMillis)
            loadData(selectedDate)
        }
    }

    private fun loadData(date: Timestamp) {
        viewModel.getTasksByDate(date).observe(viewLifecycleOwner) { listTasks ->
            tasksList = listTasks.sortedBy { it.dateStart }
            tableRowAdapter.setList(tasksList)
        }
    }

    companion object {
        fun clickTask(task: Task) {
            val bundle = Bundle()
            bundle.putSerializable(TAG_TASK, task)
            APP.navController.navigate(R.id.action_tasksFragment_to_taskFragment, bundle)
        }
    }
}