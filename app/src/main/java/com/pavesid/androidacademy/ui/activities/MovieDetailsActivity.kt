package com.pavesid.androidacademy.ui.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.data.local.FakeRepository
import com.pavesid.androidacademy.ui.adapters.CastAdapter
import com.pavesid.androidacademy.ui.decorations.MarginItemDecoration

class MovieDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        val orig = findViewById<ImageView>(R.id.orig)
        orig.load(Drawable.createFromStream(assets.open("orig.png"), null))
        val recycler = findViewById<RecyclerView>(R.id.cast_recycler)
        val castAdapter = CastAdapter()
        recycler.apply {
            layoutManager = LinearLayoutManager(this@MovieDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            addItemDecoration(
                MarginItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.spacing_extra_small_4),
                    bigSpaceSize = resources.getDimensionPixelSize(R.dimen.spacing_normal_16)
                )
            )
        }

        castAdapter.actors = FakeRepository.getEndGameActors()
    }
}
