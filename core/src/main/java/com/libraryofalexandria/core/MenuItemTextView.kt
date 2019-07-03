package com.libraryofalexandria.core

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

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