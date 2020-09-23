package com.learn.focus.pomodoro.app.ui.viewmodel

import android.content.Context
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.learn.focus.pomodoro.app.R
import com.learn.focus.pomodoro.app.data.db.DatabaseManager
import com.learn.focus.pomodoro.app.data.model.TimerTask
import com.learn.focus.pomodoro.app.repository.TimerTaskRepository
import com.learn.focus.pomodoro.app.ui.fragment.ListCompletedTasksFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(context: Context) : ViewModel() {

    private val repository: TimerTaskRepository

    init {
        val timerTaskDao = DatabaseManager.getInstance(context).getTimerDao()
        repository = TimerTaskRepository(timerTaskDao)
    }

    fun showTimerFragment(view: View, timerTask: TimerTask) {
        val action = ListCompletedTasksFragmentDirections.actionListToTimer(timerTask)

        view.findNavController().navigate(action)
    }

    fun showPopupMenu(view: View, timerTask: TimerTask) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.inflate(R.menu.popup_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit_item -> {
                    true
                }
                R.id.remove_item -> {
                    delete(timerTask)
                    true
                }
                else -> true
            }
        }

        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass
            .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
            .invoke(menu, true)

        popupMenu.show()

    }

    private fun update(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(timerTask)
        }

    private fun delete(timerTask: TimerTask) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(timerTask)
        }

}