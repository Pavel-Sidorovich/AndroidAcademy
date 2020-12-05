package com.pavesid.androidacademy.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.ui.details.MoviesDetailsFragment
import com.pavesid.androidacademy.ui.movies.MoviesFragment

class MainActivity : AppCompatActivity(), MoviesFragment.Listener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootFragment = MoviesFragment().apply { setListener(this@MainActivity) }

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

    override fun changeFragmentById(id: Int) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, MoviesDetailsFragment.newInstance(id), null)
            addToBackStack(null)
            commit()
        }
    }

    fun changeFragment(id: Int) {
        val detailFragment = MoviesDetailsFragment.newInstance(id)
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container, detailFragment, null)
            addToBackStack(null)
            commit()
        }
    }
}
