package com.pavesid.androidacademy.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView

/**
 * https://github.com/SenhLinsh/NoPaddingTextView
 */
class NoPaddingTextView : androidx.appcompat.widget.AppCompatTextView {
    private var mAdditionalPadding = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        includeFontPadding = false
    }

    override fun onDraw(canvas: Canvas) {
        val yOff = -mAdditionalPadding.toFloat() / 6
        canvas.translate(0f, yOff)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var measureSpec = heightMeasureSpec
        additionalPadding
        val mode = MeasureSpec.getMode(measureSpec)
        if (mode != MeasureSpec.EXACTLY) {
            val measureHeight = measureHeight(text.toString(), widthMeasureSpec)
            var height = measureHeight - mAdditionalPadding
            height += paddingTop + paddingBottom
            measureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, measureSpec)
    }

    private fun measureHeight(text: String, widthMeasureSpec: Int): Int {
        val textSize = textSize
        val textView = TextView(context)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        textView.text = text
        textView.measure(widthMeasureSpec, 0)
        return textView.measuredHeight
    }

    private val additionalPadding: Int
        get() {
            val textSize = textSize
            val textView = TextView(context)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            textView.setLines(1)
            textView.measure(0, 0)
            val measuredHeight = textView.measuredHeight
            if (measuredHeight - textSize > 0) {
                mAdditionalPadding = (measuredHeight - textSize).toInt()
            }
            return mAdditionalPadding
        }
}
