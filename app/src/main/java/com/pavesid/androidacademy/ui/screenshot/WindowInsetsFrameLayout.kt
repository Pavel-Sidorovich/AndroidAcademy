package com.pavesid.androidacademy.ui.screenshot

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.core.view.children

/**
 * FrameLayout which takes care of applying the window insets to child views.
 */
class WindowInsetsFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        children.iterator().forEach {
            it.dispatchApplyWindowInsets(insets)
        }
        return insets
    }
}
