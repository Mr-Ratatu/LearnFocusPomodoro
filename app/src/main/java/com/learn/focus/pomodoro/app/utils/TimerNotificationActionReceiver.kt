package com.learn.focus.pomodoro.app.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.nowSeconds
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.removeAlarm
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.setAlarm

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            AppConstants.ACTION_STOP -> {
                removeAlarm(context)
                PrefUtil.setTimerState(TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                removeAlarm(context)
                PrefUtil.setTimerState(TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = setAlarm(context, nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = (PrefUtil.getTimerLength(context))?.toInt()
                val secondsRemaining = minutesRemaining!! * 60L
                val wakeUpTime = setAlarm(context, nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_BREAK -> {
                val minutesRemaining = (PrefUtil.getShortBreakLength(context))?.toInt()
                val secondsRemaining = minutesRemaining!! * 60L
                val wakeUpTime = setAlarm(context, nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TimerState.Break, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}
