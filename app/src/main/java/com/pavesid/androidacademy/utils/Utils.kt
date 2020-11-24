package com.pavesid.androidacademy.utils

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Point
import android.graphics.Shader
import android.os.Build
import android.view.WindowManager
import android.widget.TextView

object Utils {

    fun getNavigationBarSize(context: Context): Point? {
        val appUsableSize: Point = getAppUsableScreenSize(context)
        val realScreenSize: Point = getRealScreenSize(context)

        return Point(realScreenSize.x - appUsableSize.x, realScreenSize.y - appUsableSize.y)
    }

    private fun getAppUsableScreenSize(context: Context): Point {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display
        } else {
            windowManager.defaultDisplay
        }
        val size = Point()
        display?.getSize(size)
        return size
    }

    private fun getRealScreenSize(context: Context): Point {
        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display
        } else {
            windowManager.defaultDisplay
        }
        val size = Point()
        display?.getRealSize(size)
        return size
    }

    fun getShaderForGradientTextView(textView: TextView): Shader = LinearGradient(
        0f,
        0f,
        0f,
        textView.textSize,
        intArrayOf(
            Color.parseColor("#ECECEC"),
            Color.parseColor("#C4C4C4"),
            Color.parseColor("#FFFFFF")
        ),
        null,
        Shader.TileMode.REPEAT
    )
}
