package com.libraryofalexandria.core.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.libraryofalexandria.core.R

abstract class MenuItemView<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var enabledText: Boolean

    var title: TextView
    var icon: ImageView

    var iconColor: Int = -1

    init {
        inflate(context, R.layout.menu_item_view, this)

        enabledText = true
        title = findViewById(R.id.title)
        icon = findViewById(R.id.icon)
    }


    fun tint(iconColor: Int) {
        this.iconColor = iconColor

        icon.setColorFilter(
            ContextCompat.getColor(context, iconColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    fun toggle() {
        if (enabledText) {
            title.setTextColor(context.getColor(R.color.textDisabledLight))
            icon.setColorFilter(
                ContextCompat.getColor(context, R.color.disabled),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            title.setTextColor(context.getColor(R.color.textPrimaryLight))
            tint(iconColor)
        }
        enabledText = !enabledText
    }

    abstract fun initialize(viewEntity: T)
}