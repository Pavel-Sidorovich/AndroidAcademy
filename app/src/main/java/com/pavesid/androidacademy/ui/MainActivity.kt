package com.pavesid.androidacademy.ui

import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootFragment = MoviesFragment()

        savedInstanceState ?: supportFragmentManager.beginTransaction().apply {
            add(R.id.container, rootFragment, null)
            commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
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
}
