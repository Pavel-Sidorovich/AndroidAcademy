package com.pavesid.androidacademy.utils

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.utils.Utils.getColorFromAttr

fun TextView.setShaderForGradient() {
    this.paint.shader = LinearGradient(
        0f,
        0f,
        0f,
        this.textSize,
        intArrayOf(
            getColorFromAttr(R.attr.colorText, this.context.theme),
            getColorFromAttr(R.attr.colorMiddleTextView, this.context.theme),
            getColorFromAttr(R.attr.colorPrimaryBW, this.context.theme)
        ),
        null,
        Shader.TileMode.REPEAT
    )
}
