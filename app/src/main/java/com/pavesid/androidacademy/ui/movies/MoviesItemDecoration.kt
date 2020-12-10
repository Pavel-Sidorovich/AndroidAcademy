package com.pavesid.androidacademy.ui.movies

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

internal class MoviesItemDecoration @Inject constructor(private val spaceSize: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            right = spaceSize / 2
            left = spaceSize / 2
            bottom = spaceSize
        }
    }
}
