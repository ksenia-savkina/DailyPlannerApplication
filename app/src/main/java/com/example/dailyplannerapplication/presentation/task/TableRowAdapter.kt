package com.example.dailyplannerapplication.presentation.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplannerapplication.R
import com.example.dailyplannerapplication.domain.model.Task
import com.example.dailyplannerapplication.domain.model.TaskWithTime
import java.sql.Timestamp

class TableRowAdapter : RecyclerView.Adapter<TableRowAdapter.ViewHolder>() {

    private var listTask = ArrayList<TaskWithTime>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvTask: TextView = itemView.findViewById(R.id.tv_task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.table_row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = listTask[position]
        val timeStart = getTime(task.task.dateStart)
        val timeFinish = getTime(task.task.dateFinish)
        val name = task.task.name
        holder.tvTime.text = task.time
        holder.tvTask.text = "$name, $timeStart - $timeFinish"
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            TasksFragment.clickTask(listTask[holder.adapterPosition].task)
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        holder.itemView.setOnClickListener(null)
    }

    fun setList(list: List<Task>) {
        listTask = ArrayList()
        for (task in list) {
            val startTime = task.dateStart?.hours
            val finishTime = task.dateFinish?.hours
            for (i in startTime!!..finishTime!!) {
                listTask.add(TaskWithTime(task, "$i:00"))
            }
        }
        notifyDataSetChanged()
    }

    private fun getTime(date: Timestamp?): String? {
        if (date != null) {
            val hours = date.hours
            val minutes = date.minutes
            var strMinutes = minutes.toString()

            if (minutes < 10)
                strMinutes = "0$minutes"

            return "$hours:$strMinutes"
        }
        return null
    }
}