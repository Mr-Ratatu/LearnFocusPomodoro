package com.learn.focus.pomodoro.app.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.databinding.FragmentTimerScreenBinding
import com.learn.focus.pomodoro.app.ui.viewmodel.TimerScreenViewModel
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.nowSeconds
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.removeAlarm
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.setAlarm
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.COMPLETE_TASK
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.ID
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIME
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TITLE
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TOTAL_TIME
import com.learn.focus.pomodoro.app.utils.NotificationUtil
import com.learn.focus.pomodoro.app.utils.PrefUtil
import com.learn.focus.pomodoro.app.utils.TimerState
import kotlinx.android.synthetic.main.fragment_timer_screen.*

class TimerScreenFragment : Fragment() {

    private lateinit var binding: FragmentTimerScreenBinding
    private val timerScreenViewModel: TimerScreenViewModel by viewModels()
    private val args by navArgs<TimerScreenFragmentArgs>()

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var secondsRemaining: Long = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_timer_screen,
            container,
            false
        )

        binding.apply {
            viewModel = timerScreenViewModel
            title = getTitleInScreen(sharedPreferences.getString(TITLE, null))
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        timerScreenViewModel.startTimer.observe(viewLifecycleOwner, Observer {
            timerState = it
            launchWithOrWithoutTask()
        })

        timerScreenViewModel.pauseTimer.observe(viewLifecycleOwner, Observer {
            timer.cancel()
            timerState = it
        })

        timerScreenViewModel.stopTimer.observe(viewLifecycleOwner, Observer {
            timer.cancel()
            onTimerFinished()
            removeItemId()
        })

    }

    override fun onResume() {
        super.onResume()

        initTimer()
        removeAlarm(view?.context!!)
        NotificationUtil.hideTimerNotification(view?.context!!)
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running) {
            timer.cancel()
            val wakeUpTime = setAlarm(view?.context!!, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(view?.context!!, wakeUpTime)

        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, view?.context!!)
        PrefUtil.setSecondsRemaining(secondsRemaining, view?.context!!)
        PrefUtil.setTimerState(timerState, view?.context!!)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(view?.context!!)

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running)
            PrefUtil.getSecondsRemaining(view?.context!!)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(view?.context!!)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running
        binding.title = getTitleInScreen(sharedPreferences.getString(TITLE, null))

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerFinished() {
        val timeTotal = sharedPreferences.getLong(TOTAL_TIME, 0) + (timerLengthSeconds / 60)
        val taskComplete = sharedPreferences.getInt(COMPLETE_TASK, 0) + 1

        timerState = TimerState.Stopped

        setNewTimerLength()

        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, view?.context!!)
        secondsRemaining = timerLengthSeconds

        updateCountdownUI()

        if (sharedPreferences.contains(ID)) {
            timerScreenViewModel.update(
                TimerTask(
                    sharedPreferences.getInt(ID, 0),
                    sharedPreferences.getString(TITLE, null),
                    (timerLengthSeconds / 60),
                    sharedPreferences.getInt(COLOR, 0),
                    true
                )
            )
        }
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = (PrefUtil.getTimerLength(view?.context!!))?.toInt()
        timerLengthSeconds = (lengthInMinutes!! * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(view?.context!!)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        timer_countdown.text =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun saveItemId(
        id: Int?,
        title: String?,
        time: Long?,
        color: Int?
    ) {
        val editor = sharedPreferences.edit()
        editor.apply {
            id?.let { putInt(ID, it) }
            title?.let { putString(TITLE, it) }
            time?.let { putLong(TIME, it) }
            color?.let { putInt(COLOR, it) }
        }.apply()

    }

    private fun removeItemId() {
        sharedPreferences.edit()
            .remove(ID)
            .remove(TITLE)
            .remove(TIME)
            .remove(COLOR)
            .apply()
    }

    private fun getTitleInScreen(title: String?): String {
        return if (title.isNullOrEmpty()) {
            getString(R.string.click_for_start)
        } else {
            title
        }
    }

    private fun launchWithOrWithoutTask() {
        try {
            saveItemId(
                args.timerTask.id,
                args.timerTask.titleTask,
                args.timerTask.timeValue,
                args.timerTask.colorView
            )
            startTimer()
        } catch (e: Exception) {
            startTimer()
        }
    }


}