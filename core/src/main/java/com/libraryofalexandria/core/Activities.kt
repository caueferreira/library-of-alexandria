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

    object Cards : AddressableActivity {
        override val className = "$PACKAGE_NAME.cards.view.activity.CardsActivity"
    }
}