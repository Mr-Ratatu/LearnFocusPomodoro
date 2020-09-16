package com.learn.focus.pomodoro.app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.databinding.FragmentTimerScreenBinding
import com.learn.focus.pomodoro.app.ui.viewmodel.TimerScreenViewModel
import com.learn.focus.pomodoro.app.utils.Constants.Companion.DIALOG

class TimerScreenFragment : Fragment() {

    private lateinit var binding: FragmentTimerScreenBinding
    private val timerScreenViewModel: TimerScreenViewModel by viewModels()

    private val args by navArgs<TimerScreenFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_timer_screen,
            container,
            false
        )

        binding.apply {
            viewModel = timerScreenViewModel
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}