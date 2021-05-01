package com.example.todoapp.utils

import androidx.appcompat.widget.SearchView

inline  fun SearchView.onQueasyListenerChanged(crossinline Listener:(String)->Unit){
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            Listener(newText.orEmpty())
            return true

        }

    })


}