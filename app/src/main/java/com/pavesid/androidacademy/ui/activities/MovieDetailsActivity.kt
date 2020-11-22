package com.pavesid.androidacademy.ui.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.FakeRepository
import com.pavesid.androidacademy.ui.adapters.CastAdapter
import com.pavesid.androidacademy.ui.decorations.MarginItemDecoration

class MovieDetailsActivity : AppCompatActivity() {

    private val castAdapter by lazy { CastAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        hideUi()
        initView()
    }

    private fun initView() {

        val movie = FakeRepository.getEndGameMovie()

        val orig = findViewById<ImageView>(R.id.orig)

        if (movie.image == "") {
            orig.load(Drawable.createFromStream(assets.open("orig.png"), null))
        } else {
            orig.load(movie.image)
        }

        val name = findViewById<TextView>(R.id.name)
        name.text = movie.title

        val recycler = findViewById<RecyclerView>(R.id.cast_recycler)
        recycler.apply {
            layoutManager = LinearLayoutManager(
                this@MovieDetailsActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = castAdapter
            addItemDecoration(
                MarginItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.spacing_extra_small_4),
                    bigSpaceSize = resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
                )
            )
        }

        castAdapter.actors = movie.actors
    }

    private fun hideUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}
