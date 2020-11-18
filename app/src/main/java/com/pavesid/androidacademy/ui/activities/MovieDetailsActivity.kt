package com.pavesid.androidacademy.ui.activities

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.pavesid.androidacademy.R
import com.pavesid.androidacademy.model.Actor
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

        castAdapter.actors = listOf(
            Actor(
                name = "Robert Downey, Jr",
                imageSrc = "https://upload.wikimedia.org/wikipedia/commons/a/a2/Robert_Downey%2C_Jr._SDCC_2014_%28cropped%29.jpg"
            ),
            Actor(
                name = "Chris Evans",
                imageSrc = "https://static.wikia.nocookie.net/marvelcinematicuniverse/images/b/b2/Chris_Evans.jpg"
            ),
            Actor(
                name = "Mark Ruffalo",
                imageSrc = "https://producedbyconference.com/New-York/wp-content/uploads/2019/10/Mark-Ruffalo-600.png"
            ),
            Actor(
                name = "Christopher \"Chris\" Hemsworth",
                imageSrc = "https://i.insider.com/5d4c880edcc1e7141a789532"
            ),
            Actor(
                name = "Benedict Timothy Carlton Cumberbatch",
                imageSrc = "https://upload.wikimedia.org/wikipedia/commons/6/6e/BCumberbatch_Comic-Con_2019.jpg"
            ),
            Actor(
                name = "Elizabeth Chase Olsen",
                imageSrc = "https://upload.wikimedia.org/wikipedia/commons/2/27/Elizabeth_Olsen_by_Gage_Skidmore_2.jpg"
            )
        )
    }
}
