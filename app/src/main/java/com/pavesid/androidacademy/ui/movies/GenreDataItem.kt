package com.pavesid.androidacademy.ui.movies

import com.pavesid.androidacademy.data.genres.Genre

sealed class GenreDataItem {
    abstract val id: Long
    abstract val name: String
    abstract var isChecked: Boolean

    data class GenreItem(val genre: Genre) : GenreDataItem() {
        override val id: Long = genre.id
        override val name: String = genre.name
        override var isChecked: Boolean = genre.isChecked
    }

    object HeaderItem : GenreDataItem() {
        override val id: Long = Long.MIN_VALUE
        override val name: String = ""
        override var isChecked: Boolean = true
    }
}
