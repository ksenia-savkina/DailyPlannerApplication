package com.example.dailyplannerapplication.presentation.task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.dailyplannerapplication.R
import com.example.dailyplannerapplication.core.APP
import com.example.dailyplannerapplication.core.TAG_SELECTED_DATE
import com.example.dailyplannerapplication.databinding.FragmentAddTaskBinding
import com.example.dailyplannerapplication.domain.model.Task
import java.sql.Timestamp
import java.time.LocalTime
import java.util.*

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeStartSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var timeFinishSetListener: TimePickerDialog.OnTimeSetListener
    private lateinit var date: Calendar
    private var timeStart = Calendar.getInstance()
    private var timeFinish = Calendar.getInstance()
    private var selectedDate = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        selectedDate.time = Timestamp.valueOf(arguments?.getString(TAG_SELECTED_DATE))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        val editTextDate = binding.editTextDate
        val editTextTimeStart = binding.editTextTimeStart
        val editTextTimeFinish = binding.editTextTimeFinish

        date = selectedDate
        editTextDate.setInitialDate(date)

        binding.buttonDate.setOnClickListener {
            clickButtonDate(timeStart, dateSetListener)
        }

        binding.buttonTimeStart.setOnClickListener {
            clickButtonTime(timeStart, timeStartSetListener)
        }

        binding.buttonTimeFinish.setOnClickListener {
            clickButtonTime(timeFinish, timeFinishSetListener)
        }

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                editTextDate.setInitialDate(date)
            }

        timeStartSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                timeStart.set(Calendar.HOUR_OF_DAY, hourOfDay)
                timeStart.set(Calendar.MINUTE, minute)
                editTextTimeStart.setInitialTime(timeStart)
            }

        timeFinishSetListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                timeFinish.set(Calendar.HOUR_OF_DAY, hourOfDay)
                timeFinish.set(Calendar.MINUTE, minute)
                editTextTimeFinish.setInitialTime(timeFinish)
            }

        binding.buttonSave.setOnClickListener {
            val name = binding.editTextName.text
            val description = binding.editTextDescription.text
            val date = editTextDate.text
            val timeStart = editTextTimeStart.text
            val timeFinish = editTextTimeFinish.text

            clickButtonSave(name, description, date, timeStart, timeFinish)
        }

        binding.buttonBack.setOnClickListener {
            APP.navController.navigate(R.id.action_addTaskFragment_to_tasksFragment)
        }
    }

    private fun clickButtonSave(
        nameEditable: Editable,
        descriptionEditable: Editable,
        dateEditable: Editable,
        timeStartEditable: Editable,
        timeFinishEditable: Editable
    ) {
        if (nameEditable.isEmpty()) {
            Toast.makeText(context, R.string.EmptyEditName, Toast.LENGTH_LONG).show()
            return
        }
        if (descriptionEditable.isEmpty()) {
            Toast.makeText(context, R.string.EmptyEditDescription, Toast.LENGTH_LONG).show()
            return
        }
        if (descriptionEditable.isEmpty()) {
            Toast.makeText(context, R.string.EmptyEditDescription, Toast.LENGTH_LONG).show()
            return
        }
        if (dateEditable.isEmpty()) {
            Toast.makeText(context, R.string.EmptyEditDate, Toast.LENGTH_LONG).show()
            return
        }
        if (timeStartEditable.isEmpty()) {
            Toast.makeText(context, R.string.EmptyEditDateStart, Toast.LENGTH_LONG).show()
            return
        }
        if (timeFinishEditable.isEmpty()) {
            Toast.makeText(context, R.string.EmptyEditDateFinish, Toast.LENGTH_LONG).show()
            return
        }

        timeStart.setSelectedDate(date, LocalTime.parse(timeStartEditable.toString()))
        timeFinish.setSelectedDate(date, LocalTime.parse(timeFinishEditable.toString()))

        val timeStart = Timestamp(timeStart.timeInMillis)
        val timeFinish = Timestamp(timeFinish.timeInMillis)

        if (timeStart >= timeFinish) {
            Toast.makeText(context, R.string.IncorrectDates, Toast.LENGTH_LONG).show()
            return
        }

        try {
            viewModel.createTask(
                Task(
                    id = 0,
                    dateStart = timeStart,
                    dateFinish = timeFinish,
                    name = nameEditable.toString(),
                    description = descriptionEditable.toString()
                )
            ) {}
            Toast.makeText(context, R.string.SuccessfulAdding, Toast.LENGTH_LONG).show()
            APP.navController.navigate(R.id.action_addTaskFragment_to_tasksFragment)
        } catch (ex: Exception) {
            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun Calendar.setSelectedDate(date: Calendar, time: LocalTime) {
        this.set(
            date[Calendar.YEAR],
            date[Calendar.MONTH],
            date[Calendar.DAY_OF_MONTH],
            time.hour,
            time.minute,
            time.second
        )
    }

    private fun EditText.setInitialDate(dateAndTime: Calendar) {
        this.setText(
            DateUtils.formatDateTime(
                context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
            )
        )
    }

    private fun EditText.setInitialTime(dateAndTime: Calendar) {
        this.setText(
            DateUtils.formatDateTime(
                context,
                dateAndTime.timeInMillis,
                DateUtils.FORMAT_SHOW_TIME
            )
        )
    }

    private fun clickButtonDate(
        dateAndTime: Calendar,
        dateSetListener: DatePickerDialog.OnDateSetListener,
    ) {
        DatePickerDialog(
            requireContext(), dateSetListener,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun clickButtonTime(
        dateAndTime: Calendar,
        timeSetListener: TimePickerDialog.OnTimeSetListener,
    ) {
        TimePickerDialog(
            context, timeSetListener,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        ).show()
    }
}