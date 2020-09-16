package com.learn.focus.pomodoro.app.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.google.android.material.snackbar.Snackbar
import com.learn.focus.pomodoro.app.data.db.DatabaseManager
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.repository.TimerTaskRepository
import com.learn.focus.pomodoro.app.utils.Constants.Companion.DIALOG
import com.learn.focus.pomodoro.app.extension.Event
import com.learn.focus.pomodoro.app.ui.fragment.CreatedTaskPomodoroFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatedTaskViewModel(application: Application) : AndroidViewModel(application) {

    private val _setColorView = MutableLiveData<Event<Int>>()
    val setColorView: LiveData<Event<Int>>
        get() = _setColorView

    val title: MutableLiveData<String?> = MutableLiveData()
    val colorView: MutableLiveData<Int?> = MutableLiveData(Color.RED)

    private val timerTaskRepository: TimerTaskRepository

    private val _closeDialog = MutableLiveData<Event<String>>()
    val closeDialog: LiveData<Event<String>>
        get() = _closeDialog

    init {
        val timerTaskDao = DatabaseManager.getInstance(application).getTimerDao()
        timerTaskRepository = TimerTaskRepository(timerTaskDao)
    }

    fun closeFragmentClick(view: View) {
        _closeDialog.value =
            Event(DIALOG)
    }

    fun createTaskTimer(view: View) {
        val keyBoard =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (title.value.isNullOrEmpty()) {
            keyBoard.hideSoftInputFromWindow(view.windowToken, 0)
            return
        }

        insert(TimerTask(null, title.value, 0, colorView.value, true))

        keyBoard.hideSoftInputFromWindow(view.windowToken, 0)

        _closeDialog.value =
            Event(DIALOG)
    }

    fun chooseColorClick(view: View) {
        ColorPickerDialog
            .Builder(view.context)
            .setColorShape(ColorShape.SQAURE)
            .setDefaultColor(Color.RED)
            .setTitle("Выбрать цвет")
            .setColorListener { color, colorHex ->
                // Handle Color Selection
                colorView.value = color
                _setColorView.value = Event(color)
            }
            .show()
    }

    private fun insert(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            timerTaskRepository.insert(timerTask)
        }

}