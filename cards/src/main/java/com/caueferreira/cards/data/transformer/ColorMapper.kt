package com.caueferreira.cards.data.transformer

import com.caueferreira.cards.domain.Color
import com.caueferreira.cards.domain.ManaCost

class ColorMapper {

    private val colors: HashMap<String, Color> = hashMapOf(
        "\\{W\\}" to Color.WHITE,
        "\\{U\\}" to Color.BLUE,
        "\\{B\\}" to Color.BLACK,
        "\\{R\\}" to Color.RED,
        "\\{G\\}" to Color.GREEN,
        "\\{X\\}" to Color.COLORLESS,
        "\\{C\\}" to Color.COLORLESS,
        "\\{[0-9]+\\}" to Color.COLORLESS
    )

    fun transform(manaCost: String, cmc: Double): ManaCost {
        var identity = arrayListOf<Color>()
        var cost = arrayListOf<Color>()

        colors.forEach { color ->
            val count = color.key.toRegex().findAll(manaCost).count()

            for (i in 0 until count) {
                cost.add(color.value)
                if (!identity.contains(color.value)) identity.add(color.value)
            }
        }

        if (identity.size == 0) identity.add(Color.COLORLESS)

        return ManaCost(
            manaCost,
            cmc,
            identity,
            cost
        )
    }
}