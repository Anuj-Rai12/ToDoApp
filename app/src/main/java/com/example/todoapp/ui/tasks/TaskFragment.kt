package com.example.todoapp.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.tasks.recyc.TaskAdapter
import com.example.todoapp.utils.onQueasyListenerChanged
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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_task, menu)
        val searchViews = menu.findItem(R.id.mysearchview)
        val searchRes = searchViews?.actionView as SearchView
        searchRes.onQueasyListenerChanged {
            viewModel.getStateData.value=it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId)
        {
            R.id.sort_by_name ->{
                true
            }
            R.id.sort_by_date ->{
                true
            }
            R.id.Hide_complete ->{
                item.isChecked=!item.isChecked
                    true
            }
            R.id.delete_completed ->{
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createRecyclerview() {
        binding.apply {
            recycleViewList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = taskAdapter
                setHasFixedSize(true)
            }
        }
        viewModel.getData.observe(viewLifecycleOwner){
            taskAdapter.submitList(it)
        }
    }

}