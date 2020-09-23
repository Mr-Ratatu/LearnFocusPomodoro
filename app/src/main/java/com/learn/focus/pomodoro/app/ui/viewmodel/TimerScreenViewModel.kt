package com.learn.focus.pomodoro.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.learn.focus.pomodoro.app.data.db.DatabaseManager
import com.learn.focus.pomodoro.app.data.db.TimerTaskDao
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.repository.TimerTaskRepository
import com.learn.focus.pomodoro.app.utils.TimerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimerScreenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TimerTaskRepository

    init {
        val timerTaskDao: TimerTaskDao = DatabaseManager.getInstance(application).getTimerDao()
        repository = TimerTaskRepository(timerTaskDao)
    }

    private var _startTimer = MutableLiveData<TimerState>()
    val startTimer: LiveData<TimerState>
        get() = _startTimer

    private val _stopTimer = MutableLiveData<TimerState>()
    val stopTimer: LiveData<TimerState>
        get() = _stopTimer

    private val _pauseTimer = MutableLiveData<TimerState>()
    val pauseTimer: LiveData<TimerState>
        get() = _pauseTimer

    fun startTimer() {
        _startTimer.value = TimerState.Running
    }

    fun stopTimer() {
        _stopTimer.value = TimerState.Stopped
    }

    fun update(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(timerTask)
        }

}