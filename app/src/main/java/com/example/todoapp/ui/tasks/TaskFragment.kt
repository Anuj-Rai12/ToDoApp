package com.example.todoapp.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.tasks.recyc.TaskAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var binding: FragmentTaskBinding
    private val taskAdapter by lazy {
        TaskAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskBinding.bind(view)
        createRecyclerview()

    }

    private fun createRecyclerview() {
        binding.apply {
            recycleViewList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = taskAdapter
                setHasFixedSize(true)
            }
        }
        addData()
    }

    private fun addData() {
        viewModel.dataEntry()
        viewModel.dataList_.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }
}