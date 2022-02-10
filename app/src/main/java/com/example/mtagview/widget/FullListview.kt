package com.example.mtagview.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView

class FullListview(context: Context, attributeSet: AttributeSet) : ListView(context,attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2,
            MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}