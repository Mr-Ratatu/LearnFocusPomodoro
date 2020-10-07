package com.learn.focus.pomodoro.app.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.learn.focus.pomodoro.app.utils.TimerState

class DeleteItemDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return (activity?.let {
            val builder = AlertDialog.Builder(it)
                .setMessage("Вы действительно хотите прервать таймер?")
                .setPositiveButton("Подтвердить") { _, _ ->
                    Toast.makeText(context, "Таймер прерван", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
            builder.create()
        } ?: throw IllegalStateException("Dialog cannot be null"))
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
    }

}