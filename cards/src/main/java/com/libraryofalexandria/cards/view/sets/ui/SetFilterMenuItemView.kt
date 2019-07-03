package com.libraryofalexandria.cards.view.sets.ui

import android.content.Context
import android.util.AttributeSet
import com.libraryofalexandria.core.ui.MenuItemView

class SetFilterMenuItemView(context: Context, attrs: AttributeSet?) :
    MenuItemView<FilterViewEntity>(context, attrs) {

    override fun initialize(viewEntity: FilterViewEntity) {
        icon.setImageDrawable(context.getDrawable(viewEntity.icon))
        title.text = context.getString(viewEntity.text)

        tint(viewEntity.iconColor)
    }
}