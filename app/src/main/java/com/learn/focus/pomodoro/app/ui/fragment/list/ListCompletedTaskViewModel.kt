package com.learn.focus.pomodoro.app.ui.fragment.list

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.data.db.DatabaseManager
import com.learn.focus.pomodoro.app.repository.TimerTaskRepository

class ListCompletedTaskViewModel(application: Application) : AndroidViewModel(application) {

    private val timerTaskRepository: TimerTaskRepository

    init {
        val timerTaskDao = DatabaseManager.getInstance(application).getTimerDao()
        timerTaskRepository = TimerTaskRepository(timerTaskDao)
    }

    val listTimerTask = timerTaskRepository.listTaskTimer

    val checkOnEmptyDB = timerTaskRepository.checkOnEmptyDB

    fun showCreteTaskScreen(view: View) {
        view.findNavController().navigate(R.id.action_list_to_createdTask)
    }

}