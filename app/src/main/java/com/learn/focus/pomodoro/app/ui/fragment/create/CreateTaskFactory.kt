package com.learn.focus.pomodoro.app.ui.fragment.create

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class CreateTaskFactory(
    private val application: Application,
    private val titleField: String?,
    private val color: Int?
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CreatedTaskViewModel(application, titleField, color) as T
}