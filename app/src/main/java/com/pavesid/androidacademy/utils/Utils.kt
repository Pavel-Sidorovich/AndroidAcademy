package com.pavesid.androidacademy.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

object Utils {

    @ColorInt
    fun getColorFromAttr(
        @AttrRes attrColor: Int,
        theme: Resources.Theme,
        typedValue: TypedValue = TypedValue(),
        resolveRefs: Boolean = true
    ): Int {
        theme.resolveAttribute(attrColor, typedValue, resolveRefs)
        return typedValue.data
    }
}
