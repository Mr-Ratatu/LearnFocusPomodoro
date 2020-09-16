package com.learn.focus.pomodoro.app.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timer")
data class TimerTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "task")
    val titleTask: String? = null,
    @ColumnInfo(name = "time")
    val timeValue: Int? = null,
    @ColumnInfo(name = "color")
    val colorView: Int? = null,
    @ColumnInfo(name = "done")
    val taskDone: Boolean? = null
)