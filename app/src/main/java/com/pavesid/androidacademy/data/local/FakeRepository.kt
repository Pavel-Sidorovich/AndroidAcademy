package com.pavesid.androidacademy.data.local

import com.pavesid.androidacademy.data.local.model.Actor

object FakeRepository {

    fun getEndGameActors() = listOf(
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
