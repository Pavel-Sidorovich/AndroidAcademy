package com.pavesid.androidacademy.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Resources.Theme.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    this.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}
