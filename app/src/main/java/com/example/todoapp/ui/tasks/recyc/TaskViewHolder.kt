package com.example.todoapp.ui.tasks.recyc

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Tasks
import com.example.todoapp.databinding.ItemTaskBinding

class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bindIt(tasks: Tasks, function: (Tasks, Boolean, Boolean) -> Unit) {
        binding.apply {
            checkBox.isChecked = tasks.complete
            checkBox.setOnClickListener {
                function(tasks, checkBox.isChecked, true)
            }
            imageView.isVisible = tasks.important
            textView.text = tasks.name
            textView.paint.isStrikeThruText = tasks.complete
            root.setOnClickListener {
                function(tasks, false, false)
            }
        }

    }

}