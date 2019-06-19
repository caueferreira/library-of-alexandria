package com.libraryofalexandria.core

import android.content.Intent

private const val PACKAGE_NAME = "com.libraryofalexandria"

interface AddressableActivity {
    val className: String
}

fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
        "$PACKAGE_NAME",
        addressableActivity.className
    )
}

object Activities {


    object Sets : AddressableActivity {
        override val className = "$PACKAGE_NAME.cards.view.sets.ui.SetsActivity"
    }

    object Cards : AddressableActivity {
        override val className = "$PACKAGE_NAME.cards.view.cards.ui.CardsActivity"
    }

    object About : AddressableActivity {
        override val className = "$PACKAGE_NAME.about.view.activity.AboutActivity"
    }
}