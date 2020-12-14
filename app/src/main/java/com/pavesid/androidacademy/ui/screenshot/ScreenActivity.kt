package com.pavesid.androidacademy.ui.screenshot

import android.animation.Animator
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewAnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.pavesid.androidacademy.App
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.utils.CompressBitmap
import kotlin.math.hypot

class ScreenActivity : AppCompatActivity() {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)

        val image = CompressBitmap.uncompressFromString(prefs.getString(App.SCREEN, "") ?: "")
        val screen = findViewById<ImageView>(R.id.screen)
        screen.setImageBitmap(image)
        screen.scaleType = ImageView.ScaleType.MATRIX
        screen.visibility = VISIBLE

        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(App.INIT))

        val listener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
                startCircularAnimationToRightTop(v as? ImageView, image)
            }

            override fun onViewDetachedFromWindow(v: View?) {}
        }
        screen.addOnAttachStateChangeListener(listener)
    }

    private fun startCircularAnimationToRightTop(
        view: ImageView?,
        image: Bitmap,
        toTop: Boolean = true
    ) {
        view?.let { screen ->
            val cX: Int = if (toTop) image.width else image.width / 2
            val cY: Int = if (toTop) 0 else image.height / 2
            val finalRadius = if (toTop) hypot(
                cX.toDouble(),
                image.height.toDouble()
            ).toFloat() else hypot(cX.toDouble(), cY.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(
                screen,
                cX,
                cY,
                finalRadius,
                0f
            )
            anim.duration = 1000
            anim.interpolator = CubicBezierInterpolator.EASE_BOTH
            val animationListener = object : Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    screen.setImageDrawable(null)
                    screen.visibility = View.GONE
                    LocalBroadcastManager.getInstance(this@ScreenActivity)
                        .sendBroadcast(Intent(App.FINISH))
                    finish()
                }

                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            }
            anim.addListener(animationListener)
            anim.start()
        }
    }
}
