package com.pavesid.androidacademy.utils

import android.view.Surface
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.round
import kotlin.math.sqrt

object CalculationAngle {

    fun getAngle(gravity: FloatArray, rotation: Int): Int {
        val normalizerGravity =
            sqrt(gravity[0] * gravity[0] + gravity[1] * gravity[1] + gravity[2] * gravity[2])

        gravity[0] = gravity[0] / normalizerGravity
        gravity[1] = gravity[1] / normalizerGravity
        gravity[2] = gravity[2] / normalizerGravity

        var rotationAngle =
            round(Math.toDegrees(atan2(gravity[0].toDouble(), gravity[1].toDouble()))).toInt()

        rotationAngle = when (rotation) {
            Surface.ROTATION_0 ->
                rotationAngle
            Surface.ROTATION_90 ->
                rotationAngle - 90
            Surface.ROTATION_270 ->
                rotationAngle + 90
            else -> rotationAngle
        }

        rotationAngle /= ANGLE_DIVIDER

        return if (abs(rotationAngle) < MAX_ANGLE) {
            rotationAngle
        } else {
            Int.MAX_VALUE
        }
    }


    private const val ANGLE_DIVIDER = 5
    private const val MAX_ANGLE = 10
}
