package com.pavesid.androidacademy.ui.dialogs

import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateUtils
import android.widget.TextView
import com.pavesid.androidacademy.utils.extensions.clearHours
import java.util.Calendar

class DatePicker(private val context: Context, private val view: TextView, initTime: Long = 0L) {

    private var dateAndTime: Calendar? = null

    val time: Long
        get() = dateAndTime?.clearHours()?.timeInMillis ?: 0L

    init {
        dateAndTime = Calendar.getInstance().clearHours()
        if (initTime != 0L) {
            (dateAndTime as Calendar).timeInMillis = initTime
        }
    }

    private val dateListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            dateAndTime?.set(Calendar.YEAR, year)
            dateAndTime?.set(Calendar.MONTH, monthOfYear)
            dateAndTime?.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            setInitialDateTime()
        }

    fun setDate() {
        dateAndTime?.let {
            DatePickerDialog(
                context, dateListener,
                it.get(Calendar.YEAR),
                it.get(Calendar.MONTH),
                it.get(Calendar.DAY_OF_MONTH)
            )
                .show()
        }
    }

    private fun setInitialDateTime() {
        view.text = DateUtils.formatDateTime(
            context,
            dateAndTime?.timeInMillis ?: Calendar.getInstance().clearHours().timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        )
    }
}
