package com.libraryofalexandria.core.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.libraryofalexandria.core.R
import kotlinx.android.synthetic.main.menu_item_view.view.*

abstract class MenuItemView<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var enabledText: Boolean

    var titleTextView: TextView
    var iconImageView: ImageView

    var iconColor: Int = -1

    init {
        inflate(context, R.layout.menu_item_view, this)

        enabledText = true
        titleTextView = title
        iconImageView = icon
    }


    fun tint(iconColor: Int) {
        this.iconColor = iconColor

        iconImageView.setColorFilter(
            ContextCompat.getColor(context, iconColor),
            android.graphics.PorterDuff.Mode.SRC_IN
        )
    }

    fun toggle() {
        if (enabledText) {
            titleTextView.setTextColor(context.getColor(R.color.textDisabledLight))
            iconImageView.setColorFilter(
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