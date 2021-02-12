package com.pavesid.androidacademy.utils.extensions

import java.util.Calendar

fun Calendar.clearHours(): Calendar {
    this.apply {
        clear(Calendar.HOUR_OF_DAY)
        clear(Calendar.MINUTE)
        clear(Calendar.SECOND)
        clear(Calendar.MILLISECOND)
    }
    return this
}

fun Calendar.clearDays(): Calendar {
    this.apply {
        clear(Calendar.YEAR)
        clear(Calendar.MONTH)
        clear(Calendar.DAY_OF_MONTH)
        clear(Calendar.DAY_OF_WEEK)
        clear(Calendar.ZONE_OFFSET)
        clear(Calendar.SECOND)
        clear(Calendar.MILLISECOND)
    }
    return this
}
