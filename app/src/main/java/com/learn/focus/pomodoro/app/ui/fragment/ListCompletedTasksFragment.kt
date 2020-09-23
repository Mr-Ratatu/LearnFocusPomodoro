package com.learn.focus.pomodoro.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.databinding.FragmentListCompletedTasksBinding
import com.learn.focus.pomodoro.app.ui.adapter.TimerTaskAdapter
import com.learn.focus.pomodoro.app.ui.viewmodel.ListCompletedTaskViewModel

class ListCompletedTasksFragment : Fragment() {

    private val listViewModel: ListCompletedTaskViewModel by viewModels()
    private lateinit var binding: FragmentListCompletedTasksBinding

    private val timerTaskAdapter = TimerTaskAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_list_completed_tasks,
            container,
            false
        )

        binding.apply {
            viewModel = listViewModel
            adapter = timerTaskAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listViewModel.listTimerTask.observe(viewLifecycleOwner, Observer {
            timerTaskAdapter.setData(it)
        })

        listViewModel.checkOnEmptyDB.observe(viewLifecycleOwner, Observer {
            binding.dbSize = it
        })

        listViewModel.showDialog.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { text ->
                CreatedTaskPomodoroFragment().show(childFragmentManager, text)
            }
        })

    }

}