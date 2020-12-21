package com.pavesid.androidacademy.utils

import android.os.SystemClock
import android.view.MenuItem
import android.view.View

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeClick: () -> Unit
) : View.OnClickListener, MenuItem.OnMenuItemClickListener {

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return true
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick()
        return false
    }

    companion object {
        private var lastTimeClicked: Long = 0
    }
}
