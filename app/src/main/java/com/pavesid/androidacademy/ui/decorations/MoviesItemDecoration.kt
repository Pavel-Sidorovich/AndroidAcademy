package com.pavesid.androidacademy.ui.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MoviesItemDecoration(private val spaceSize: Int, private val bigSpaceSize: Int, private val gridSize: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val count = (parent.adapter?.itemCount ?: 1) - 1
        val lastIndexInNotLastLine = (count / gridSize) * gridSize
        with(outRect) {
//            if (((parent.adapter?.itemCount ?: 1) - 1) % 2 == 0) {
//
//            }
            val position = parent.getChildAdapterPosition(view)
            bottom =
                if (position >= lastIndexInNotLastLine) {
                    bigSpaceSize
                } else spaceSize
        }
    }
}
