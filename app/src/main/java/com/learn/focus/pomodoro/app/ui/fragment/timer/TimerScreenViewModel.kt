package com.learn.focus.pomodoro.app.ui.fragment.timer

import android.app.Application
import android.util.Log
import android.widget.Toast
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

    private val _pausedTimer = MutableLiveData<TimerState>()
    val pausedTimer: LiveData<TimerState>
        get() = _pausedTimer

    private val _breakTimer = MutableLiveData<TimerState>()
    val breakTimer: LiveData<TimerState>
        get() = _breakTimer

    fun startTimer() {
        _startTimer.value = TimerState.Running
    }

    fun stopTimer() {
        _stopTimer.value = TimerState.Stopped
    }

    fun pausedTimer() {
        _pausedTimer.value = TimerState.Paused
    }

    fun breakTimer() {
        _breakTimer.value = TimerState.Running
    }

    fun update(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(timerTask)
        }

}