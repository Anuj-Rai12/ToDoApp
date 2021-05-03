package com.example.todoapp.ui.tasks

import androidx.recyclerview.widget.RecyclerView


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.data.SortOrder
import com.example.todoapp.data.Tasks
import com.example.todoapp.databinding.FragmentTaskBinding
import com.example.todoapp.ui.tasks.dialog.MyDialogFrag
import com.example.todoapp.ui.tasks.recyc.TaskAdapter
import com.example.todoapp.utils.onQueasyListenerChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {
    private val viewModel: TaskViewModel by activityViewModels()
    private lateinit var binding: FragmentTaskBinding
    private lateinit var taskAdapter: TaskAdapter


    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskBinding.bind(view)
        createRecyclerview()
        setHasOptionsMenu(true)
        binding.fabAddTask.setOnClickListener {
            viewModel.initial()
            goToAddFragment()
        }
    }

    private fun goToAddFragment() {
        val action = TaskFragmentDirections.actionTaskFragmentToTaskEditAddFragment("New Tasks")
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_task, menu)
        val searchViews = menu.findItem(R.id.mysearchview)
        val searchRes = searchViews?.actionView as SearchView
        searchRes.onQueasyListenerChanged {
            viewModel.getStateData.value = it
        }
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.Hide_complete).isChecked =
                viewModel.preferencesFlow.first().HIDE_COMPLETED
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_name -> {
                viewModel.sortOrderData(SortOrder.BY_NAME)
                true
            }
            R.id.sort_by_date -> {
                viewModel.sortOrderData(SortOrder.BY_DATE)
                true
            }
            R.id.Hide_complete -> {
                item.isChecked = !item.isChecked
                viewModel.hideOrderCompeted(item.isChecked)
                true
            }
            R.id.delete_completed -> {
                findNavController().navigate(R.id.action_global_myDialogFrag)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @ExperimentalCoroutinesApi
    private fun createRecyclerview() {
        binding.apply {
            recycleViewList.apply {
                layoutManager = LinearLayoutManager(requireContext())
                taskAdapter = TaskAdapter { select: Tasks, click: Boolean, option: Boolean ->
                    preferMyChoice(
                        select,
                        click,
                        option
                    )
                }
                adapter = taskAdapter
                setHasFixedSize(true)
            }
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val currPos = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.deleteTaskData(currPos)
                }
            }).attachToRecyclerView(recycleViewList)
        }

        viewModel.getData.observe(viewLifecycleOwner) { op ->
            taskAdapter.submitList(op)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.taskEvent.collect { event ->
                when (event) {
                    is TaskViewModel.TaskEvent.ShowTasksMessage -> {
                        Snackbar.make(requireView(), "Deleted The Task", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.addNewTasks(event.tasks)
                            }.show()
                    }
                }
            }
        }
    }

    private fun preferMyChoice(tasks: Tasks, click: Boolean, option: Boolean) {
        when (option) {
            true -> {
                checkBoxClicked(tasks, click)
            }
            false -> {
                itemClicked(tasks, option)
            }
        }
    }

    private fun itemClicked(tasks: Tasks, option: Boolean) {
        viewModel.itemClicked(tasks, option)
        val action = TaskFragmentDirections.actionTaskFragmentToTaskEditAddFragment("Edit Tasks")
        findNavController().navigate(action)
    }

    private fun checkBoxClicked(tasks: Tasks, click: Boolean) {
        viewModel.updateCheckBox(tasks, click)
    }
}