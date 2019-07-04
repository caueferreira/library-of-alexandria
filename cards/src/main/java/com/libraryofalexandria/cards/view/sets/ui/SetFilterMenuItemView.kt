package com.libraryofalexandria.cards.view.sets.ui

import android.content.Context
import android.util.AttributeSet
import com.libraryofalexandria.core.ui.MenuItemView

class SetFilterMenuItemView(context: Context, attrs: AttributeSet?) :
    MenuItemView<FilterViewEntity>(context, attrs) {

    override fun initialize(viewEntity: FilterViewEntity) {
        iconImageView.setImageDrawable(context.getDrawable(viewEntity.icon))
        titleTextView.text = context.getString(viewEntity.text)

        tint(viewEntity.iconColor)
    }
}