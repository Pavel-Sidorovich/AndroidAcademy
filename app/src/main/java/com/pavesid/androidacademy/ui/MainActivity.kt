package com.pavesid.androidacademy.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.pavesid.androidacademy.App
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import com.pavesid.androidacademy.ui.screenshot.ScreenActivity
import com.pavesid.androidacademy.utils.CompressBitmap
import com.pavesid.androidacademy.utils.extensions.ExitWithAnimation
import com.pavesid.androidacademy.utils.extensions.exitCircularReveal
import com.pavesid.androidacademy.utils.extensions.exitCircularRevealToLeft
import com.pavesid.androidacademy.utils.extensions.open
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.hypot

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    private var isDarkTheme = false

    private var detailsIsOpen = false

    private var themeIsChanging = false

    private val initScreenReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                recreate()
            }
        }
    }

    private val closeScreenReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                themeIsChanging = false
            }
        }
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkTheme = prefs.getBoolean(THEME, false)
        changeTheme()

        val rootFragment = MoviesFragment()

        savedInstanceState ?: supportFragmentManager.open {
            add(R.id.container, rootFragment, null)
        }

        LocalBroadcastManager.getInstance(this).apply {
            registerReceiver(initScreenReceiver, IntentFilter(App.INIT))
            registerReceiver(closeScreenReceiver, IntentFilter(App.FINISH))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).apply {
            unregisterReceiver(initScreenReceiver)
            unregisterReceiver(closeScreenReceiver)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.theme -> {
                if (!themeIsChanging) {
                    themeIsChanging = true
                    isDarkTheme = !isDarkTheme
                    prefs.edit().putBoolean(THEME, isDarkTheme).apply()
                    changeTheme(findViewById(R.id.theme))
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun changeFragment(parcelable: Parcelable, cX: Int, cY: Int) {
        if (!detailsIsOpen) {
            detailsIsOpen = true
            val detailFragment = MoviesDetailsFragment.newInstance(parcelable, cX, cY)
            supportFragmentManager.open {
                add(R.id.container, detailFragment, null)
                addToBackStack(null)
            }
        }
    }

    override fun onBackPressed() {
        with(supportFragmentManager.findFragmentById(R.id.container)) {
            if ((this as? ExitWithAnimation)?.isToBeExitedWithAnimation() == true) {
                detailsIsOpen = false
                if (this.posX == null || this.posY == null) {
                    this.view?.exitCircularRevealToLeft {
                        super.onBackPressed()
                    }
                } else {
                    this.view?.exitCircularReveal(this.posX!!, this.posY!!) {
                        super.onBackPressed()
                    } ?: super.onBackPressed()
                }
            } else {
                super.onBackPressed()
            }
        }
    }

    private fun changeTheme(view: View? = null) {
        view?.let {
            val windowBitmap = Bitmap.createBitmap(
                window.decorView.width,
                window.decorView.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(windowBitmap)
            window.decorView.draw(canvas)

            val location = IntArray(2)
            it.getLocationOnScreen(location)
            prefs.edit().putString(App.SCREEN, CompressBitmap.compressToString(windowBitmap))
                .apply()
            val intent = Intent(this, ScreenActivity::class.java).apply {
                putExtra(App.POS_X, location[0] + it.width / 2)
                putExtra(App.POS_Y, location[1] + it.width / 2)
                putExtra(
                    App.RADIUS,
                    hypot(
                        window.decorView.width.toDouble(),
                        window.decorView.height.toDouble()
                    ).toFloat()
                )
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            overridePendingTransition(0, 0)
            startActivity(intent)
        }
        delegate.localNightMode =
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
    }

    private companion object {
        const val THEME = "switchTheme"
    }
}
