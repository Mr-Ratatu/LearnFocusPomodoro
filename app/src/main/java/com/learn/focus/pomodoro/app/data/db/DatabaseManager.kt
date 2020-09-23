package com.learn.focus.pomodoro.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.DB_NAME

@Database(entities = [TimerTask::class], version = 3, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun getTimerDao(): TimerTaskDao

    companion object {
        @Volatile
        private var instance: DatabaseManager? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, DatabaseManager::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

}