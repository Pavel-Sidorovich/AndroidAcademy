package com.pavesid.androidacademy.ui.dialogs

import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateUtils
import android.widget.TextView
import com.pavesid.androidacademy.R
import java.util.Calendar

class DatePicker(private val context: Context, private val view: TextView, initTime: Long = 0L) {

    private var dateAndTime: Calendar? = null

    val calendar: Calendar
        get() = dateAndTime ?: Calendar.getInstance()

    init {
        dateAndTime = Calendar.getInstance()
        if (initTime != 0L) {
            (dateAndTime as Calendar).timeInMillis = initTime
        }
        setInitialDateTime()
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
                context,
                R.style.PickerDialog,
                dateListener,
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
            dateAndTime?.timeInMillis ?: 0L,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        )
    }
}
