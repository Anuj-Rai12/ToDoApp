package com.example.todoapp.ui.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskEditAddFragment : Fragment(R.layout.fragment_add_edit_task) {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var binding: FragmentAddEditTaskBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditTaskBinding.bind(view)
        displayValues()
    }

    @SuppressLint("SetTextI18n")
    private fun displayValues(): Boolean {
        viewModel.getRes?.let {
            viewModel.getLiveDataNow.observe(viewLifecycleOwner) { task ->
                binding.apply {
                    addEditTask.setText(task.name)
                    checkImp.isChecked = task.important
                    checkImp.jumpDrawablesToCurrentState()
                    taskDate.apply {
                        visibility = View.VISIBLE
                        text = "Created :${task.createdCurrentTimeData}"
                    }
                }
            }
            return true
        }
        return false
    }

}