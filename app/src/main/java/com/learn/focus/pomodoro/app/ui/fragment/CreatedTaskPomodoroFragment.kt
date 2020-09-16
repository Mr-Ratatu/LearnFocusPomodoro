package com.learn.focus.pomodoro.app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.databinding.FragmentAddTaskBinding
import com.learn.focus.pomodoro.app.ui.viewmodel.CreatedTaskViewModel

class CreatedTaskPomodoroFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val createdTaskViewModel: CreatedTaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)

        binding.viewModel = createdTaskViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        createdTaskViewModel.closeDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                dismiss()
            }
        })

        createdTaskViewModel.setColorView.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {color ->
                binding.colorBar.setBackgroundColor(color)
            }
        })
    }

}