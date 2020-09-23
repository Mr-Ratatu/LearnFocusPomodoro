package com.learn.focus.pomodoro.app.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "timer")
@Parcelize
data class TimerTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "task")
    val titleTask: String?,
    @ColumnInfo(name = "time")
    val timeValue: Long?,
    @ColumnInfo(name = "color")
    val colorView: Int?,
    @ColumnInfo(name = "done")
    val taskDone: Boolean?
) : Parcelable