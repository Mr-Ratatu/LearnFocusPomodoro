package com.learn.focus.pomodoro.app.repository

import com.learn.focus.pomodoro.app.data.db.TimerTaskDao
import com.learn.focus.pomodoro.app.data.model.TimerTask

class TimerTaskRepository(private val timerTaskDao: TimerTaskDao) {

    val listTaskTimer = timerTaskDao.getAllListTimerTask()

    val checkOnEmptyDB = timerTaskDao.checkOnEmptyDB()

    suspend fun insert(timerTask: TimerTask) {
        timerTaskDao.insert(timerTask)
    }

    suspend fun update(timerTask: TimerTask) {
        timerTaskDao.update(timerTask)
    }

    suspend fun delete(timerTask: TimerTask) {
        timerTaskDao.delete(timerTask)
    }

}