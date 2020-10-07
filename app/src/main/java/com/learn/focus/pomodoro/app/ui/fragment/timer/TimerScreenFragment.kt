package com.learn.focus.pomodoro.app.ui.fragment.timer

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.databinding.FragmentTimerScreenBinding
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.nowSeconds
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.removeAlarm
import com.learn.focus.pomodoro.app.utils.AlarmUtils.Companion.setAlarm
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.ID
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.STATUS_BAR_COLOR
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIME
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TIMER_WORK_OR_BREAK
import com.learn.focus.pomodoro.app.utils.AppConstants.Companion.TITLE
import com.learn.focus.pomodoro.app.utils.NotificationUtil
import com.learn.focus.pomodoro.app.utils.PrefUtil
import com.learn.focus.pomodoro.app.utils.TimerState
import com.learn.focus.pomodoro.app.utils.WorkState


class TimerScreenFragment : Fragment() {

    private lateinit var binding: FragmentTimerScreenBinding
    private val timerScreenViewModel: TimerScreenViewModel by viewModels()
    private val args by navArgs<TimerScreenFragmentArgs>()

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped
    private var workState = WorkState.Work
    private var secondsRemaining: Long = 0
    private var breakTime: Long = 0
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
            backgroundColor = PrefUtil.getBackgroundColor(requireContext())
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        try {
            if (!args.timerTask.titleTask.isNullOrEmpty()) {
                launchWithOrWithoutTask()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        timerScreenViewModel.apply {
            startTimer.observe(viewLifecycleOwner, Observer {
                startOrBreakTimer(it, WorkState.Work)
            })

            stopTimer.observe(viewLifecycleOwner, Observer {
                showDialogStopTimer()
            })

            pausedTimer.observe(viewLifecycleOwner, Observer {
                timer.cancel()
                timerState = it
                updateUiButton()
            })

            breakTimer.observe(viewLifecycleOwner, Observer {
                startOrBreakTimer(it, WorkState.Break)
            })

        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()
        removeAlarm(requireContext())
        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running) {
            timer.cancel()
            val wakeUpTime = setAlarm(requireContext(), nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(requireContext(), wakeUpTime)
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, requireContext())
        PrefUtil.setSecondsRemaining(secondsRemaining, requireContext())
        PrefUtil.setTimerState(timerState, requireContext())
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(requireContext())

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = when (timerState) {
            TimerState.Running,
            TimerState.Paused -> PrefUtil.getSecondsRemaining(
                requireContext()
            )
            else -> timerLengthSeconds
        }

        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext())
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        when {
            secondsRemaining <= 0 -> onTimerFinished()
            timerState == TimerState.Running -> startTimer()
        }

        updateUiButton()
        updateCountdownUI()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = when (workState) {
            WorkState.Work ->
                (PrefUtil.getTimerLength(requireContext()))?.toInt()
            else -> PrefUtil.getShortBreakLength(requireContext())?.toInt()
        }
        timerLengthSeconds = (lengthInMinutes!! * 60L)
        binding.max = when (PrefUtil.getTimerWorkOrBreak(requireContext())) {
            WorkState.Work -> timerLengthSeconds.toInt()
            else -> (PrefUtil.getShortBreakLength(requireContext())?.toLong()!! * 60L).toInt()
        }
    }

    private fun startTimer() {
        timerState = TimerState.Running
        binding.title = getTitleInScreen(sharedPreferences.getString(TITLE, null))
        setNewTimerLength()
        val remaining = when (workState) {
            WorkState.Work -> {
                secondsRemaining
            }
            else -> {
                PrefUtil.getShortBreakLength(requireContext())?.toLong()!! * 60L
            }
        }

        timer = object : CountDownTimer(remaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun onTimerStopped() {
        timer.cancel()
        timerState = TimerState.Stopped
        workState = WorkState.Work

        setNewTimerLength()

        binding.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
        secondsRemaining = timerLengthSeconds

        updateUiButton()
        updateCountdownUI()
        removeItemId()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        workState = WorkState.Work

        setNewTimerLength()

        binding.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, requireContext())
        secondsRemaining = timerLengthSeconds

        updateUiButton()
        updateCountdownUI()

        removeItemId()
        setAlarm(requireContext(), nowSeconds, secondsRemaining)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(requireContext())
        binding.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.timerCountDown =
            "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0$secondsStr"}"
        binding.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun saveItemId(
        id: Int?,
        title: String?,
        time: Long?,
        color: Int?
    ) {
        sharedPreferences.edit {
            id?.let { putInt(ID, it) }
            title?.let { putString(TITLE, it) }
            time?.let { putLong(TIME, it) }
            color?.let { putInt(COLOR, it) }
        }

    }

    private fun removeItemId() {
        sharedPreferences.edit()
            .remove(ID)
            .remove(TITLE)
            .remove(TIME)
            .remove(COLOR)
            .remove(TIMER_WORK_OR_BREAK)
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

    private fun updateUiButton() {
        when (timerState) {
            TimerState.Running -> {
                binding.startTimer.visibility = View.GONE
                binding.stopTimer.visibility = View.VISIBLE
                binding.paused.visibility = View.VISIBLE
                binding.timerBreak.visibility = View.GONE
            }
            TimerState.Stopped -> {
                binding.startTimer.visibility = View.VISIBLE
                binding.stopTimer.visibility = View.GONE
                binding.paused.visibility = View.GONE
                binding.timerBreak.visibility = View.VISIBLE
            }
            TimerState.Paused -> {
                binding.startTimer.visibility = View.VISIBLE
                binding.stopTimer.visibility = View.VISIBLE
                binding.paused.visibility = View.GONE
                binding.timerBreak.visibility = View.GONE
            }
            TimerState.Break -> {
                binding.startTimer.visibility = View.GONE
                binding.stopTimer.visibility = View.VISIBLE
                binding.paused.visibility = View.VISIBLE
                binding.timerBreak.visibility = View.GONE
            }
        }
    }

    private fun showDialogStopTimer() {
        val builder = AlertDialog.Builder(context)
        builder
            .setMessage("Вы действительно хотите прервать таймер?")
            .setPositiveButton("Да") { _, _ ->
                onTimerStopped()
            }
            .setNegativeButton("Нет", null)
            .create()
            .show()
    }

    private fun startOrBreakTimer(it: TimerState, state: WorkState) {
        workState = state
        PrefUtil.setTimerWorkOrBreak(workState, requireContext())
        timerState = it
        launchWithOrWithoutTask()
        updateUiButton()
    }


}