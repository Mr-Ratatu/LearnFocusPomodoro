package com.learn.focus.pomodoro.app.ui.fragment.create

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.databinding.FragmentAddTaskBinding

class CreatedTaskPomodoroFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding

    private val args by navArgs<CreatedTaskPomodoroFragmentArgs>()
    private lateinit var createdTaskViewModel: CreatedTaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_task, container, false)

        createdTaskViewModel = ViewModelProvider(
            this,
            CreateTaskFactory(
                requireActivity().application,
                args.timerTaskUpdate?.titleTask,
                args.timerTaskUpdate?.colorView
            )
        ).get(CreatedTaskViewModel::class.java)

        binding.apply {
            viewModel = createdTaskViewModel

            if (args.timerTaskUpdate != null)
                timerTask = args.timerTaskUpdate
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        createdTaskViewModel.setColorView.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                ColorPickerDialog
                    .Builder(view?.context!!)
                    .setColorShape(ColorShape.SQAURE)
                    .setDefaultColor(Color.RED)
                    .setTitle("Выбрать цвет")
                    .setColorListener { color, colorHex ->
                        // Handle Color Selection
                        createdTaskViewModel.colorView.value = color
                        binding.colorBar.setBackgroundColor(color)
                    }
                    .show()

            }
        })
    }

}