package com.learn.focus.pomodoro.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.learn.focus.pomodoro.app.data.db.DatabaseManager
import com.learn.focus.pomodoro.app.extension.Event
import com.learn.focus.pomodoro.app.repository.TimerTaskRepository
import com.learn.focus.pomodoro.app.utils.Constants.Companion.DIALOG

class ListCompletedTaskViewModel(application: Application) : AndroidViewModel(application) {

    private val _showDialog = MutableLiveData<Event<String>>()
    val showDialog: LiveData<Event<String>>
        get() = _showDialog

    private val timerTaskRepository: TimerTaskRepository

    init {
        val timerTaskDao = DatabaseManager.getInstance(application).getTimerDao()
        timerTaskRepository = TimerTaskRepository(timerTaskDao)
    }

    val listTimerTask = timerTaskRepository.listTaskTimer
    val checkOnEmptyDB = timerTaskRepository.checkOnEmptyDB

    fun showCreteTaskDialog() {
        _showDialog.value = Event(DIALOG)
    }

}