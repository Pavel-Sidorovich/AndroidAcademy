package com.pavesid.androidacademy.utils.extensions

import android.content.SharedPreferences

inline fun SharedPreferences.edit(block: SharedPreferences.Editor.() -> Unit) {
    this.edit().apply {
        block()
        apply()
    }
}
