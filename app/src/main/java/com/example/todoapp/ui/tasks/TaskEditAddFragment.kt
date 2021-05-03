package com.example.todoapp.ui.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddEditTaskBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskEditAddFragment : Fragment(R.layout.fragment_add_edit_task) {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var binding: FragmentAddEditTaskBinding
    private var name: String? = null
    private var imp: Boolean = false
    private var id: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditTaskBinding.bind(view)
        displayValues()
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
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayValues() {
        viewModel.getRes?.let {
            viewModel.getLiveDataNow.observe(viewLifecycleOwner) { task ->
                id = task.id
                binding.apply {
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

}