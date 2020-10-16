package com.learn.focus.pomodoro.app.extension

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
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

fun View.snackBar(message: String, action: (() -> Unit)? = null) {
    val view = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let {
        view.setAction("Вернуть") {
            it()
        }
    }
    view.show()
}

fun Fragment.vibratePhone() {
    val vibration = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26)
        vibration.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
    else
        vibration.vibrate(2000)
}

