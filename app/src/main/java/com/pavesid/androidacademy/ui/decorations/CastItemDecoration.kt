package com.pavesid.androidacademy.ui.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CastItemDecoration(private val spaceSize: Int, private val bigSpaceSize: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            left = if (parent.getChildAdapterPosition(view) == 0) {
                bigSpaceSize
            } else spaceSize

            right =
                if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 1) - 1) {
                    bigSpaceSize
                } else spaceSize
        }
    }
}
