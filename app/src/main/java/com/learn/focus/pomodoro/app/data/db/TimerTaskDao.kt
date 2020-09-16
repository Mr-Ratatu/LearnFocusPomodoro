package com.learn.focus.pomodoro.app.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.learn.focus.pomodoro.app.data.model.TimerTask

@Dao
interface TimerTaskDao {

    @Query("SELECT COUNT(*) FROM timer")
    fun checkOnEmptyDB() : LiveData<Int>

    @Query("SELECT * FROM timer")
    fun getAllListTimerTask(): LiveData<List<TimerTask>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(timerTask: TimerTask)

    @Delete
    suspend fun delete(timerTask: TimerTask)
}