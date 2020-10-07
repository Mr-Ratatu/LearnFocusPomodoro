package com.learn.focus.pomodoro.app.binding

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.extension.gone

@BindingAdapter("emptyListVisible")
fun setEmptyListVisible(view: View, emptyList: Int) {
    view.gone(emptyList)
}

@BindingAdapter("adapter")
fun setRecyclerAdapter(recyclerView: RecyclerView, timerTaskAdapter: RecyclerView.Adapter<*>) {
    recyclerView.apply {
        adapter = timerTaskAdapter
        hasFixedSize()
    }
}

@BindingAdapter("titleVisible")
fun setTitleVisible(view: MaterialTextView, title: String?) {
    view.gone(title)
}

@BindingAdapter("taskDone")
fun setTaskDone(view: MaterialTextView, done: Boolean?) {
    when (done) {
        true ->
            view.apply {
                setText(R.string.taskDone)
                setTextColor(view.resources.getColor(R.color.taskDone, null))
            }
        else ->
            view.apply {
                setText(R.string.inProgress)
                setTextColor(view.resources.getColor(R.color.inProgress, null))
            }
    }
}

@BindingAdapter("visibleButton")
fun setVisibleButton(view: View, visible: Boolean) {
    view.gone(visible)
}

@BindingAdapter("setTitleFragment")
fun setTitleFragment(view: MaterialTextView, field: MutableLiveData<String>) {
    if (field.value.isNullOrEmpty())
        view.setText(R.string.create_item)
    else
        view.setText(R.string.edit_task)
}
