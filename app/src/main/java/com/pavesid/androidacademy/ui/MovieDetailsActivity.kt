package com.pavesid.androidacademy.ui

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.model.Cast
import com.pavesid.androidacademy.ui.adapters.CastAdapter

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
        }

        castAdapter.casts = listOf(
            Cast(
                name = "Robert Downey, Jr",
                imageSrc = "https://upload.wikimedia.org/wikipedia/commons/a/a2/Robert_Downey%2C_Jr._SDCC_2014_%28cropped%29.jpg"
            ),
            Cast(
                name = "Chris Evans",
                imageSrc = "https://static.wikia.nocookie.net/marvelcinematicuniverse/images/b/b2/Chris_Evans.jpg"
            ),
            Cast(
                name = "Mark Ruffalo",
                imageSrc = "https://producedbyconference.com/New-York/wp-content/uploads/2019/10/Mark-Ruffalo-600.png"
            ),
            Cast(
                name = "Christopher \"Chris\" HemsworthVeryLong",
                imageSrc = "https://i.insider.com/5d4c880edcc1e7141a789532"
            )
        )
    }
}