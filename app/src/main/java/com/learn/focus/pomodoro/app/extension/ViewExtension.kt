package com.learn.focus.pomodoro.app.extension

import android.view.View
import com.learn.focus.pomodoro.app.R

fun View.gone(emptyList: Int) {
    visibility = when (emptyList) {
        0 -> View.VISIBLE
        else -> View.GONE
    }
}

fun View.gone(visibleButton: Boolean) {
    visibility = when (visibleButton) {
        false -> View.VISIBLE
        else -> View.INVISIBLE
    }
}

fun View.gone(title: String?) {
    visibility = if (title.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}


