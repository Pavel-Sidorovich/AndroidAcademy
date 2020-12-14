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
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.hypot

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    private var isDarkTheme = false

    private val initScreenReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                recreate()
            }
        }
    }

    private val finishAnimReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                themeIsChanging = false
            }
        }
    }

    private var themeIsChanging = false
    private var fragmentIsChanging = false

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDarkTheme = prefs.getBoolean(THEME, false)
        changeTheme()

        val rootFragment = MoviesFragment()

        savedInstanceState ?: supportFragmentManager.beginTransaction().apply {
            add(R.id.container, rootFragment, null)
            commit()
        }

        LocalBroadcastManager.getInstance(this).apply {
            registerReceiver(initScreenReceiver, IntentFilter(App.INIT))
            registerReceiver(finishAnimReceiver, IntentFilter(App.FINISH))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).apply {
            unregisterReceiver(initScreenReceiver)
            unregisterReceiver(finishAnimReceiver)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                fragmentIsChanging = false
                true
            }
            R.id.theme -> {
                if (!themeIsChanging && !fragmentIsChanging) {
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

    fun changeFragment(parcelable: Parcelable) {
        if (!themeIsChanging && !fragmentIsChanging) {
            fragmentIsChanging = true
            val detailFragment = MoviesDetailsFragment.newInstance(parcelable)
            supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.slide_out
                )
                add(R.id.container, detailFragment, null)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun changeTheme(view: View? = null) {
        view?.let {
            val windowBitmap = Bitmap.createBitmap(window.decorView.width, window.decorView.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(windowBitmap)
            window.decorView.draw(canvas)

            val location = IntArray(2)
            it.getLocationOnScreen(location)
            prefs.edit().putString(App.SCREEN, CompressBitmap.compressToString(windowBitmap))
                .apply()
            val intent = Intent(this, ScreenActivity::class.java).apply {
                putExtra(App.POS_X, location[0] + it.width / 2)
                putExtra(App.POS_Y, location[1] + it.width / 2)
                putExtra(App.RADIUS, hypot(window.decorView.width.toDouble(), window.decorView.height.toDouble()).toFloat())
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
