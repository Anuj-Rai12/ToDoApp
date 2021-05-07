package com.example.todoapp.ui.tasks

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.alarmes.servi.AlarmService
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TaskEditAddFragment : Fragment(R.layout.fragment_add_edit_task) {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var binding: FragmentAddEditTaskBinding
    private var name: String? = null
    private var imp: Boolean = false
    private var id: Int? = null
    private var getTime: Long? = null
    private var getPos: Int? = null
    private val alarmService by lazy {
        AlarmService(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditTaskBinding.bind(view)
        displayValues()
        setDropdown()
        viewModel.getMsgLiveDataNow.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { str ->
                Snackbar.make(requireView(), str, Snackbar.LENGTH_SHORT).show()
                if (str != "Enter Some Value")
                    findNavController().navigate(R.id.action_taskEditAddFragment_to_taskFragment)
            }
        }
        binding.apply {

            addEditTask.addTextChangedListener {
                name = it.toString()
            }
            checkImp.setOnCheckedChangeListener { _, isChecked ->
                imp = isChecked

            }
            fadCreate.setOnClickListener {
                viewModel.onAddOrUpdateItem(name, imp, id)
                val stringOp = addEditTask.text.toString()
                getAlarm { function ->
                    alarmService.setExactAlarm(function, stringOp)
                }
                getRepeating { function, dur ->
                    alarmService.setRepetitiveAlarm(function, stringOp, dur)
                }
            }
            timer.setOnClickListener {
                if (!checkImp.isChecked) {
                    Toast.makeText(activity, "Not A Important task", Toast.LENGTH_SHORT).show()
                } else if (addEditTask.text.toString().isBlank()) {
                    Toast.makeText(activity, "Enter your task Name", Toast.LENGTH_SHORT).show()
                } else {
                    setAlarm()
                    layoutdrop.visibility = View.VISIBLE
                }
            }
            binding.Autocom.setOnItemClickListener { _, _, position, _ ->
                getPosition(position)
                Log.i("MYTIME", "this is your -> $position")
            }
        }
    }

    private fun getPosition(position: Int) {
        getPos = when (position) {
            1 -> 1

            2 -> 7

            3 -> 30

            4 -> 356

            else -> null
        }
        Log.i("MyTIME", "The value of getPos -> $getPos")
    }

    private fun setDropdown() {
        val weeks = resources.getStringArray(R.array.timers)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdaown, weeks)
        binding.Autocom.setAdapter(arrayAdapter)
    }

    private fun getAlarm(function: (Long) -> Unit) {
        getTime?.let {
            function(it)
        }
    }

    private fun getRepeating(function: (Long, Int) -> Unit) {
        getTime?.let { lon ->
            getPos?.let { int ->
                function(lon, int)
            }
        }
    }

    private fun setAlarm() {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    Log.i("MyTime", "$year convert to Long ${year.toLong()}")
                    Log.i("MyTime", "$month convert to Long ${month.toLong()}")
                    Log.i("MyTime", "$day convert to Long ${day.toLong()}")
                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            getTime =
                                this.timeInMillis + year.toLong()
                            +month.toLong()
                            +day.toLong()
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayValues() {
        viewModel.getRes?.let {
            viewModel.getLiveDataNow.observe(viewLifecycleOwner) { task ->
                id = task.id
                binding.apply {
                    timer.visibility = View.GONE
                    layoutdrop.visibility = View.GONE
                    addEditTask.setText(task.name)
                    checkImp.isChecked = task.important
                    checkImp.jumpDrawablesToCurrentState()
                    taskDate.apply {
                        visibility = View.VISIBLE
                        text = "Created : ${task.createdCurrentTimeData}"
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setDropdown()
    }
}