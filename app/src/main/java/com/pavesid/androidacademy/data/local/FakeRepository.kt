package com.pavesid.androidacademy.data.local

import com.pavesid.androidacademy.data.local.model.Actor
import com.pavesid.androidacademy.data.local.model.Movie
import com.pavesid.androidacademy.data.local.model.MoviePreview

object FakeRepository : MoviesRepository {

    /**
     * Return mockup movie by Id
     */
    override fun getMovieById(id: Int): Movie {
        return when (id % 4) {
            0 ->
                Movie(
                    id = 0,
                    image = "https://s3.amazonaws.com/prod.assets.thebanner/styles/article-large/s3/article/large/MM-037%20Avengers_%20Endgame.jpg",
                    actors = listOf(
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
                    ),
                    storyline = "After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos' actions and restore balance to the universe."
                )
            1 ->
                Movie(
                    id = 1,
                    image = "https://newsabc.net/wp-content/uploads/2020/08/1598683220_Why-you-should-watch-Tenet-in-the-cinema-even-if-780x470.jpg",
                    actors = listOf(
                        Actor(
                            name = "John David Washington",
                            imageSrc = "https://st.kp.yandex.net/im/kadr/3/5/5/kinopoisk.ru-John-David-Washington-3558411.jpg"
                        ),
                        Actor(
                            name = "Robert Pattinson",
                            imageSrc = "https://st.kp.yandex.net/im/kadr/3/5/4/kinopoisk.ru-Robert-Pattinson-3540779.jpg"
                        ),
                        Actor(
                            name = "Elizabeth Debicki",
                            imageSrc = "https://st.kp.yandex.net/im/kadr/3/5/5/kinopoisk.ru-Elizabeth-Debicki-3552504.jpg"
                        ),
                        Actor(
                            name = "Kenneth Branagh",
                            imageSrc = "https://st.kp.yandex.net/im/kadr/1/2/4/kinopoisk.ru-Kenneth-Branagh-1241673.jpg"
                        )
                    ),
                    storyline = "Armed with only one word, Tenet, and fighting for the survival of the entire world, a Protagonist journeys through a twilight world of international espionage on a mission that will unfold in something beyond real time."
                )
            2 ->
                Movie(
                    id = 2,
                    image = "https://cdn.mos.cms.futurecdn.net/4L75tkWVDgKtGe7kfoEGP6-1024-80.jpg.webp",
                    actors = listOf(
                        Actor(
                            name = "Scarlett Johansson",
                            imageSrc = "https://s1.r29static.com/bin/entry/cf2/340x408,85/2220755/image.webp"
                        ),
                        Actor(
                            name = "Florence Pugh",
                            imageSrc = "https://toronto.citynews.ca/wp-content/blogs.dir/sites/10/2019/06/NYET414-618_2019_013921.jpg"
                        ),
                        Actor(
                            name = "Rachel Weisz",
                            imageSrc = "https://upload.wikimedia.org/wikipedia/commons/7/7f/Rachel_Weisz_2018.jpg"
                        ),
                        Actor(
                            name = "David Harbour",
                            imageSrc = "https://upload.wikimedia.org/wikipedia/commons/8/84/David_Harbour_by_Gage_Skidmore.jpg"
                        )
                    ),
                    storyline = "A film about Natasha Romanoff in her quests between the films Civil War and Infinity War."
                )
            else ->
                Movie(
                    id = 3,
                    image = "https://www.okino.ua/media/var/news/2020/03/25/Wonder_Woman_1984.jpg",
                    actors = listOf(
                        Actor(
                            name = "Pedro Pascal",
                            imageSrc = "https://upload.wikimedia.org/wikipedia/commons/c/c5/Pedro_Pascal_by_Gage_Skidmore.jpg"
                        ),
                        Actor(
                            name = "Gal Gadot",
                            imageSrc = "https://1.bp.blogspot.com/-tCrT1NchogI/WhDAfukqS8I/AAAAAAACKuk/v8ZT-5L3T10-Vf74huzt5I2iv3q9pS6SwCLcBGAs/s1600/Elle_USA_-_December_2017-1.jpg"
                        ),
                        Actor(
                            name = "Connie Nielsen",
                            imageSrc = "https://upload.wikimedia.org/wikipedia/commons/2/2c/Connie_Nielsen_by_Gage_Skidmore.jpg"
                        ),
                        Actor(
                            name = "Robin Wright",
                            imageSrc = "https://upload.wikimedia.org/wikipedia/commons/d/d9/Robin_Wright_Cannes_2017_%28cropped%29.jpg"
                        )
                    ),
                    storyline = "Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah.Fast forward to the 1980s as Wonder Woman's next big screen adventure finds her facing two all-new foes: Max Lord and The Cheetah."
                )
        }
    }

    /**
     * Return all previews for our movies
     */
    override fun getAllPreviews(): List<MoviePreview> {
        val mutableList = ArrayList<MoviePreview>()
        for (i in 0 until 3) {
            mutableList.addAll(
                listOf(
                    MoviePreview(
                        id = 4 * i,
                        image = "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg",
                        name = "Avengers: End Game",
                        rating = 4,
                        pg = 13,
                        reviews = 135,
                        duration = 137,
                        tags = listOf(
                            "Action",
                            "Adventure",
                            "Fantasy"
                        )
                    ),
                    MoviePreview(
                        id = 4 * i + 1,
                        image = "https://upload.wikimedia.org/wikipedia/en/1/14/Tenet_movie_poster.jpg",
                        name = "Tenet",
                        rating = 5,
                        pg = 16,
                        reviews = 98,
                        duration = 97,
                        tags = listOf(
                            "Action",
                            "Sci-Fi",
                            "Thriller"
                        )
                    ),
                    MoviePreview(
                        id = 4 * i + 2,
                        image = "https://terrigen-cdn-dev.marvel.com/content/prod/1x/blackwidow_lob_crd_04.jpg",
                        name = "Black Widow",
                        rating = 4,
                        pg = 13,
                        reviews = 38,
                        duration = 102,
                        tags = listOf(
                            "Action",
                            "Adventure",
                            "Sci-Fi"
                        )
                    ),
                    MoviePreview(
                        id = 4 * i + 3,
                        image = "https://upload.wikimedia.org/wikipedia/ru/6/67/Wonder_Woman_1984_%28poster%29.jpg",
                        name = "Wonder Woman 1984",
                        rating = 5,
                        pg = 13,
                        reviews = 74,
                        duration = 120,
                        tags = listOf(
                            "Action",
                            "Adventure",
                            "Fantasy"
                        )
                    )
                )
            )
        }
        return mutableList
    }
}
