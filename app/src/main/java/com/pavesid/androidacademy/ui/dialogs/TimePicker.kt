package com.pavesid.androidacademy.ui.dialogs

import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateUtils
import android.widget.TextView
import com.pavesid.androidacademy.R
import java.util.Calendar

class TimePicker(private val context: Context, private val view: TextView, initTime: Long = 0L) {

    private var dateAndTime: Calendar? = null

    val calendar: Calendar
        get() = dateAndTime ?: Calendar.getInstance()

    init {
        dateAndTime = Calendar.getInstance()
        if (initTime != 0L) {
            (dateAndTime as Calendar).timeInMillis = initTime
        }
    }

    private val timeListener =
        TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            dateAndTime?.set(Calendar.HOUR_OF_DAY, hourOfDay)
            dateAndTime?.set(Calendar.MINUTE, minute)
            setInitialDateTime()
        }

    fun setTime() {
        dateAndTime?.let {
            TimePickerDialog(
                context,
                R.style.PickerDialog,
                timeListener,
                it.get(Calendar.HOUR_OF_DAY),
                it.get(Calendar.MINUTE),
                true
            )
                .show()
        }
    }

    private fun setInitialDateTime() {
        view.text = DateUtils.formatDateTime(
            context,
            dateAndTime?.timeInMillis ?: 0L,
            DateUtils.FORMAT_SHOW_TIME
        )
    }
}
