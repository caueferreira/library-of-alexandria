package com.libraryofalexandria.core.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.libraryofalexandria.core.R

class MenuItemTextView(context: Context, attrs: AttributeSet) : TextView(context, attrs) {

    private var enabledText: Boolean

    init {
        enabledText = true
    }

    fun toggle() {
        if (enabledText) {
            setTextColor(context.getColor(R.color.textDisabledLight))
        } else {
            setTextColor(context.getColor(R.color.textPrimaryLight))
        }
        enabledText = !enabledText
    }
}