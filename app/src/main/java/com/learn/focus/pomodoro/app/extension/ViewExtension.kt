package com.learn.focus.pomodoro.app.extension

import android.view.View

fun View.gone(emptyList: Int) {
    visibility = when (emptyList) {
        0 -> View.VISIBLE
        else -> View.GONE
    }
}

fun View.gone(title: String?) {
    visibility = if (title.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

