package com.pavesid.androidacademy.utils.extensions

import android.view.MenuItem
import com.pavesid.androidacademy.utils.SafeClickListener

/**
 * Prevent user from doing multiple clicks on a menuItem.
 */
fun MenuItem.setSafeOnMenuItemClickListener(onSafeClick: () -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick()
    }
    setOnMenuItemClickListener(safeClickListener)
}
