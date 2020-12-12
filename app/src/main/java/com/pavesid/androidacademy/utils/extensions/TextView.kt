package com.pavesid.androidacademy.utils.extensions

import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView
import com.pavesid.androidacademy.R

fun TextView.setShaderForGradient() {
    this.paint.shader = LinearGradient(
        0f,
        0f,
        0f,
        this.textSize,
        intArrayOf(
            this.context.theme.getColorFromAttr(R.attr.colorText),
            this.context.theme.getColorFromAttr(R.attr.colorMiddleTextView),
            this.context.theme.getColorFromAttr(R.attr.colorPrimaryBW)
        ),
        null,
        Shader.TileMode.REPEAT
    )
}
