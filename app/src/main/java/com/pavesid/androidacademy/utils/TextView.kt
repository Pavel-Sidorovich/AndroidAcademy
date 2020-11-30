package com.pavesid.androidacademy.utils

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.widget.TextView

fun TextView.setShaderForGradient() {
    this.paint.shader = LinearGradient(
        0f,
        0f,
        0f,
        this.textSize,
        intArrayOf(
            Color.parseColor("#ECECEC"),
            Color.parseColor("#C4C4C4"),
            Color.parseColor("#FFFFFF")
        ),
        null,
        Shader.TileMode.REPEAT
    )
}
