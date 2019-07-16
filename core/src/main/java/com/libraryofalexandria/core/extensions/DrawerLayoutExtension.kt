package com.libraryofalexandria.core.extensions

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

fun DrawerLayout.toggle() {
    if (isDrawerOpen(GravityCompat.END))
        closeDrawers()
    else
        openDrawer(GravityCompat.END)
}