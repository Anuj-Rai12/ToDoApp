package com.example.todoapp.ui.tasks.recyc

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.data.Tasks

class RecycleDiffUtils : DiffUtil.ItemCallback<Tasks>() {
    override fun areItemsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Tasks, newItem: Tasks): Boolean {
        return oldItem == newItem
    }
}