package com.learn.focus.pomodoro.app.utils

import androidx.recyclerview.widget.DiffUtil
import com.learn.focus.pomodoro.app.data.model.TimerTask

class TimerDiffUtils(
    private val oldList: List<TimerTask>,
    private val newList: List<TimerTask>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] === newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].titleTask == newList[newItemPosition].titleTask
                && oldList[oldItemPosition].timeValue == newList[newItemPosition].timeValue
                && oldList[oldItemPosition].colorView == newList[newItemPosition].colorView
                && oldList[oldItemPosition].taskDone == newList[newItemPosition].taskDone


}