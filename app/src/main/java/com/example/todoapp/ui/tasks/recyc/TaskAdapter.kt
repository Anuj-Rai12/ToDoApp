package com.example.todoapp.ui.tasks.recyc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.data.Tasks
import com.example.todoapp.databinding.ItemTaskBinding

class TaskAdapter(private val function: (Tasks, Boolean, Boolean) -> Unit) : ListAdapter<Tasks, TaskViewHolder>(RecycleDiffUtils()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currPos = getItem(position)
        holder.bindIt(currPos,function)
    }
}