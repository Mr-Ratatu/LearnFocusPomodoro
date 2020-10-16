package com.learn.focus.pomodoro.app.ui.fragment.settings

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.BACKGROUND_COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.MENU_COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.STATUS_BAR_COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIMER_BREAK_SHORT
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIMER_LENGTH
import com.learn.focus.pomodoro.app.utils.Event
import kotlinx.android.synthetic.main.fragment_settings_app.view.*

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val keyBoard =
        application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val timerValue: MutableLiveData<String?> = MutableLiveData("25")
    val timerBreakValue: MutableLiveData<String?> = MutableLiveData("5")
    val color = MutableLiveData(R.drawable.main_background)
    val statusBar = MutableLiveData(R.color.colorPrimary)

    private val _backgroundColor = MutableLiveData<Event<Int>>()
    val backgroundColor: LiveData<Event<Int>>
        get() = _backgroundColor

    private val _statusBarColor = MutableLiveData<Event<Int>>()
    val statusBarColor: LiveData<Event<Int>>
        get() = _statusBarColor

    init {
        val edit = PreferenceManager.getDefaultSharedPreferences(application)
        timerValue.value = edit.getString(TIMER_LENGTH, "25")
        timerBreakValue.value = edit.getString(TIMER_BREAK_SHORT, "5")
        color.value = edit.getInt(BACKGROUND_COLOR, R.drawable.main_background)
        statusBar.value = edit.getInt(STATUS_BAR_COLOR, R.color.colorPrimary)
    }

    fun saveTimerValues(view: View) {
        if (timerValue.value.isNullOrEmpty()
            || timerBreakValue.value.isNullOrEmpty()
        ) return

        val edit = PreferenceManager.getDefaultSharedPreferences(view.context)
        edit.edit {
            putString(TIMER_LENGTH, timerValue.value)
            putString(TIMER_BREAK_SHORT, timerBreakValue.value)
        }

        keyBoard.hideSoftInputFromWindow(view.windowToken, 0)
        Toast.makeText(view.context, "Настройки таймера сохранены", Toast.LENGTH_SHORT).show()
    }

    fun setBackgroundColor(view: View) {
        _backgroundColor.value = Event(Color.RED)
    }

    fun setStatusBarColor(view: View) {
        _statusBarColor.value = Event(Color.RED)
    }

    fun resetSettings(view: View) {
        val edit = PreferenceManager.getDefaultSharedPreferences(view.context).edit()
        edit.apply {
            putString(TIMER_LENGTH, "25")
            putString(TIMER_BREAK_SHORT, "5")
            remove(BACKGROUND_COLOR)
            remove(STATUS_BAR_COLOR)
        }.apply()

        Toast.makeText(view.context, "Настройки сброшены", Toast.LENGTH_SHORT).show()
        view.findNavController().navigate(R.id.action_settings_to_timer)
    }

}