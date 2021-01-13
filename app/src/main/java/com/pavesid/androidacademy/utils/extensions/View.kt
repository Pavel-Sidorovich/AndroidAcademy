package com.pavesid.androidacademy.utils.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import com.pavesid.androidacademy.utils.SafeClickListener
import kotlin.math.hypot
import android.view.View.OnLayoutChangeListener as OnLayoutChangeListener1

/**
 * Starts circular reveal animation
 *
 * @param startX: Animation start point X coordinate.
 * @param startY: Animation start point Y coordinate.
 */
fun View.startCircularReveal(startX: Int, startY: Int) {
    addOnLayoutChangeListener(object : OnLayoutChangeListener1 {
        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            v.removeOnLayoutChangeListener(this)
            val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
            ViewAnimationUtils.createCircularReveal(v, startX, startY, 0f, radius.toFloat()).apply {
                interpolator = DecelerateInterpolator(2f)
                duration = 400
                start()
            }
        }
    })
}

/**
 * Starts circular reveal animation
 */
fun View.startCircularRevealFromLeft() {
    addOnLayoutChangeListener(object : OnLayoutChangeListener1 {
        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            v.removeOnLayoutChangeListener(this)
            val cx = v.left
            val cy = v.bottom
            val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
            ViewAnimationUtils.createCircularReveal(v, cx, cy, radius.toFloat(), 0f).apply {
                interpolator = DecelerateInterpolator(2f)
                duration = 400
                start()
            }
        }
    })
}

/**
 * Animate fragment exit using given parameters as animation end point. Runs the given block of code
 * after animation completion.
 *
 * @param exitX: Animation end point X coordinate.
 * @param exitY: Animation end point Y coordinate.
 * @param block: Block of code to be executed on animation completion.
 */
fun View.exitCircularReveal(exitX: Int, exitY: Int, block: () -> Unit) {
    val startRadius = hypot(this.width.toDouble(), this.height.toDouble())
    ViewAnimationUtils.createCircularReveal(this, exitX, exitY, startRadius.toFloat(), 0f).apply {
        duration = 400
        interpolator = DecelerateInterpolator(1f)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                block()
                super.onAnimationEnd(animation)
            }
        })
        start()
    }
}

/**
 * Animate fragment exit. Runs the given block of code
 * after animation completion.
 *
 * @param block: Block of code to be executed on animation completion.
 */
fun View.exitCircularRevealToLeft(block: () -> Unit) {
    val startRadius = hypot(this.right.toDouble(), this.bottom.toDouble())
    val exitX = this.left
    val exitY = this.bottom
    ViewAnimationUtils.createCircularReveal(this, exitX, exitY, startRadius.toFloat(), 0f).apply {
        duration = 400
        interpolator = DecelerateInterpolator(1f)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                block()
                super.onAnimationEnd(animation)
            }
        })
        start()
    }
}

/**
 * Needs to be implemented by fragments that should exit with circular reveal
 * animation along with [isToBeExitedWithAnimation] returning true
 * @property posX the X-axis position of the center of circular reveal
 * @property posY the Y-axis position of the center of circular reveal
 */
interface ExitWithAnimation {
    var posX: Int?
    var posY: Int?
    /**
     * Must return true if required to exit with circular reveal animation
     */
    fun isToBeExitedWithAnimation(): Boolean
}

/**
 * Prevent user from doing multiple clicks on a view.
 */
fun View.setSafeOnClickListener(onSafeClick: () -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick()
    }
    setOnClickListener(safeClickListener)
}

/**
 * Hide keyboard
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}
