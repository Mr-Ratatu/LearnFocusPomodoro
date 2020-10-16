package com.learn.focus.pomodoro.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.databinding.TimerTaskItemBinding
import com.learn.focus.pomodoro.app.utils.TimerDiffUtils

class TimerTaskAdapter : RecyclerView.Adapter<TimerTaskAdapter.TimerTaskViewHolder>() {

    private var items = emptyList<TimerTask>()

    fun setData(list: List<TimerTask>) {
        val timerDiffUtils = TimerDiffUtils(items, list)
        val timerDiffResult = DiffUtil.calculateDiff(timerDiffUtils)
        this.items = list
        timerDiffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerTaskViewHolder =
        TimerTaskViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.timer_task_item,
                parent,
                false
            )
        )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TimerTaskViewHolder, position: Int) {
        holder.binding.apply {
            timerTask = items[position]
            itemViewModel =
                ItemViewModel(
                    holder.itemView.context
                )

            executePendingBindings()
        }
    }

    class TimerTaskViewHolder(val binding: TimerTaskItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}