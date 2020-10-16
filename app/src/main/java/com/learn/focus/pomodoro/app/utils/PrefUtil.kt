package com.learn.focus.pomodoro.app.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.BACKGROUND_COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.MENU_COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIMER_BREAK_SHORT
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIMER_LENGTH
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIMER_WORK_OR_BREAK


class PrefUtil {
    companion object {

        fun getTimerLength(context: Context): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(TIMER_LENGTH, "25")
        }

        fun getShortBreakLength(context: Context): String? {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(TIMER_BREAK_SHORT, "5")
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
                .apply()
        }


        private const val TIMER_STATE_ID = "timer_state"

        fun getTimerState(context: Context): TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
                .apply()
        }

        fun getTimerWorkOrBreak(context: Context) : WorkState{
            val preference = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preference.getInt(TIMER_WORK_OR_BREAK, 0)
            return WorkState.values()[ordinal]
        }

        fun setTimerWorkOrBreak(workState: WorkState, context: Context) {
            val preference = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = workState.ordinal
            preference.edit {
                putInt(TIMER_WORK_OR_BREAK, ordinal)
            }
        }

        private const val SECONDS_REMAINING_ID = "seconds_remaining"

        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }


        private const val ALARM_SET_TIME_ID = "backgrounded_time"

        fun getAlarmSetTime(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }

        fun getBackgroundColor(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(BACKGROUND_COLOR, R.drawable.main_background)
        }

        fun setStyleInApp(context: Context, key: String, value: Int) {
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
            preferenceManager.edit {
                putInt(key, value)
            }
        }
    }
}