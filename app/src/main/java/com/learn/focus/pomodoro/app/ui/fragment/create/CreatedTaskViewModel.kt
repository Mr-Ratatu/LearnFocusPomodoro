package com.learn.focus.pomodoro.app.ui.fragment.create

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.learn.focus.pomodoro.app.data.db.DatabaseManager
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.repository.TimerTaskRepository
import com.learn.focus.pomodoro.app.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatedTaskViewModel(application: Application, titleField: String?, color: Int?) :
    AndroidViewModel(application) {

    private val _setColorView = MutableLiveData<Event<Int>>()
    val setColorView: LiveData<Event<Int>>
        get() = _setColorView

    val title: MutableLiveData<String?> = MutableLiveData()
    val colorView: MutableLiveData<Int?> = MutableLiveData(Color.RED)

    private val timerTaskRepository: TimerTaskRepository

    init {
        val timerTaskDao = DatabaseManager.getInstance(application).getTimerDao()
        timerTaskRepository = TimerTaskRepository(timerTaskDao)

        if (!titleField.isNullOrEmpty()) {
            title.value = titleField
            colorView.value = color
        }
    }

    fun closeFragmentClick(view: View) {
        view.findNavController().popBackStack()
    }


    fun createTaskTimer(view: View, timerTask: TimerTask?) {
        val keyBoard =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (title.value.isNullOrEmpty()) {
            keyBoard.hideSoftInputFromWindow(view.windowToken, 0)
            Snackbar.make(view, "Поле не может быть пустым", Snackbar.LENGTH_SHORT).show()
            return
        }

        if (timerTask == null)
            insert(TimerTask(null, title.value, 0, colorView.value, false))
        else
            update(
                TimerTask(
                    timerTask.id,
                    title.value,
                    timerTask.timeValue,
                    colorView.value,
                    timerTask.taskDone
                )
            )

        keyBoard.hideSoftInputFromWindow(view.windowToken, 0)
        view.findNavController().popBackStack()
    }


    fun chooseColorClick() {
        _setColorView.value = Event(Color.RED)
    }

    private fun insert(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            timerTaskRepository.insert(timerTask)
        }

    private fun update(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            timerTaskRepository.update(timerTask)
        }
}