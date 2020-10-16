package com.learn.focus.pomodoro.app.ui.fragment.settings

import android.graphics.Color
import android.graphics.Color.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.databinding.FragmentSettingsAppBinding
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.BACKGROUND_COLOR
import com.learn.focus.pomodoro.app.utils.PrefUtil
import com.learn.focus.pomodoro.app.utils.PrefUtil.Companion.setStyleInApp

class SettingsAppFragment : Fragment() {

    private lateinit var binding: FragmentSettingsAppBinding

    private val settingsViewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings_app, container, false)

        binding.viewModel = settingsViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        settingsViewModel.apply {

            backgroundColor.observe(viewLifecycleOwner, Observer {
                it.getContentIfNotHandled()?.let {
                    MaterialColorPickerDialog
                        .Builder(view?.context!!)
                        .setColorShape(ColorShape.SQAURE)
                        .setTitle("Выбрать цвет")
                        .setColorListener { color, _ ->
                            settingsViewModel.color.value = color
                            binding.backgroundColor.setBackgroundColor(color)
                            setStyleInApp(requireContext(), BACKGROUND_COLOR, color)
                        }
                        .show()
                }
            })

        }
    }


}