package com.pavesid.androidacademy.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.jraska.falcon.Falcon
import com.pavesid.androidacademy.App
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.databinding.ActivityMainBinding
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment
import com.pavesid.androidacademy.ui.screenshot.ScreenActivity
import com.pavesid.androidacademy.utils.CompressBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(applicationContext) }

    private var isDarkTheme = false

    private val receiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                recreate()
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

        savedInstanceState ?: supportFragmentManager.beginTransaction().apply {
            add(R.id.container, rootFragment, null)
            commit()
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(App.INIT))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.theme -> {
                isDarkTheme = !isDarkTheme
                prefs.edit().putBoolean(THEME, isDarkTheme).apply()
                changeTheme(true)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun changeFragment(parcelable: Parcelable) {
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

    private fun changeTheme(needScreen: Boolean = false) {
        if (needScreen) {
            val windowBitmap = Falcon.takeScreenshotBitmap(this)
            prefs.edit().putString(App.SCREEN, CompressBitmap.compressToString(windowBitmap))
                .apply()
            val intent = Intent(this, ScreenActivity::class.java).apply {
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
