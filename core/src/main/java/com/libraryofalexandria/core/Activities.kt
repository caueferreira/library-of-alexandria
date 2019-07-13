package com.libraryofalexandria.core

import android.content.Intent

private const val PACKAGE_NAME = "com.libraryofalexandria"

interface AddressableActivity {
    val className: String
}

fun intentTo(addressableActivity: AddressableActivity): Intent =
    Intent(Intent.ACTION_VIEW).setClassName(
        "$PACKAGE_NAME",
        addressableActivity.className
    )

object Activities {

    object Expansions {

        object Main : AddressableActivity {
            override val className = "$PACKAGE_NAME.cards.view.expansions.ui.ExpansionsActivity"
        }
    }

    object Cards : AddressableActivity {
        const val total: String = "TOTAL"
        const val expansion: String = "EXPANSION"

        override val className = "$PACKAGE_NAME.cards.view.cards.ui.CardsActivity"
    }

    object About : AddressableActivity {
        override val className = "$PACKAGE_NAME.about.view.activity.AboutActivity"
    }
}