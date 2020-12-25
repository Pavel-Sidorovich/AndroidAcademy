package com.pavesid.androidacademy.utils

import android.view.Surface
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CalculationAngleTest {

    @Test
    fun `Should return 0 when rotation is 0`() {
        val angle = CalculationAngle.getAngle(floatArrayOf(0f, 0f, 0f), Surface.ROTATION_0)
        assertThat(angle).isEqualTo(0)
    }

    @Test
    fun `Should return 0 when rotation is 90`() {
        val angle = CalculationAngle.getAngle(floatArrayOf(90f, 0f, 0f), Surface.ROTATION_90)
        assertThat(angle).isEqualTo(0)
    }

    @Test
    fun `Should return 0 when rotation is 270`() {
        val angle = CalculationAngle.getAngle(floatArrayOf(-90f, 0f, 0f), Surface.ROTATION_270)
        assertThat(angle).isEqualTo(0)
    }

    @Test
    fun `Should return MAX_VALUE when rotation is 180`() {
        val angle = CalculationAngle.getAngle(floatArrayOf(-180f, 0f, 0f), Surface.ROTATION_180)
        assertThat(angle).isEqualTo(Int.MAX_VALUE)
    }
}
