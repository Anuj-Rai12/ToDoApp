package com.example.todoapp.ui.tasks.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.todoapp.ui.tasks.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyDialogFrag : DialogFragment() {
    private val viewModel: TaskViewModel by activityViewModels()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alterDialog =
            android.app.AlertDialog.Builder(requireContext()).setTitle("Confirmation Required")
                .setMessage("Do You Really Want To Delete All Completed Tasks")
                .setNegativeButton("No") { option, _ ->
                    option.dismiss()
                }
                .setPositiveButton("Yes") { _, _ ->
                    viewModel.deleteAll()
                }
        return alterDialog.create()
    }
}