package com.pavesid.androidacademy.ui.movies

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class MoviesItemDecoration(private val spaceSize: Int) :
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
